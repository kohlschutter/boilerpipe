/*
 * Copyright 2001-2008 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cyberneko.html;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * This class is duplicated for each JAXP subpackage so keep it in sync.
 * It is package private and therefore is not exposed as part of the JAXP
 * API.
 * <p>
 * This code is designed to implement the JAXP 1.1 spec pluggability
 * feature and is designed to run on JDK version 1.1 and
 * later, and to compile on JDK 1.2 and onward.
 * The code also runs both as part of an unbundled jar file and
 * when bundled as part of the JDK.
 * <p>
 *
 * @version $Id: ObjectFactory.java,v 1.1 2004/03/31 20:00:21 andyc Exp $
 */
class ObjectFactory {

    //
    // Constants
    //

    // name of default properties file to look for in JDK's jre/lib directory
    private static final String DEFAULT_PROPERTIES_FILENAME = "xerces.properties";

    /** Set to true for debugging */
    private static final boolean DEBUG = false;
    
    /**
     * Default columns per line.
     */
    private static final int DEFAULT_LINE_LENGTH = 80;

    /** cache the contents of the xerces.properties file.
     *  Until an attempt has been made to read this file, this will
     * be null; if the file does not exist or we encounter some other error
     * during the read, this will be empty.
     */
    private static Properties fXercesProperties = null;

    /***
     * Cache the time stamp of the xerces.properties file so
     * that we know if it's been modified and can invalidate
     * the cache when necessary.
     */
    private static long fLastModified = -1;

    //
    // static methods
    //

    /**
     * Finds the implementation Class object in the specified order.  The
     * specified order is the following:
     * <ol>
     *  <li>query the system property using <code>System.getProperty</code>
     *  <li>read <code>META-INF/services/<i>factoryId</i></code> file
     *  <li>use fallback classname
     * </ol>
     *
     * @return Class object of factory, never null
     *
     * @param factoryId             Name of the factory to find, same as
     *                              a property name
     * @param fallbackClassName     Implementation class name, if nothing else
     *                              is found.  Use null to mean no fallback.
     *
     * @exception ObjectFactory.ConfigurationError
     */
    static Object createObject(String factoryId, String fallbackClassName)
        throws ConfigurationError {
        return createObject(factoryId, null, fallbackClassName);
    } // createObject(String,String):Object

    /**
     * Finds the implementation Class object in the specified order.  The
     * specified order is the following:
     * <ol>
     *  <li>query the system property using <code>System.getProperty</code>
     *  <li>read <code>$java.home/lib/<i>propertiesFilename</i></code> file
     *  <li>read <code>META-INF/services/<i>factoryId</i></code> file
     *  <li>use fallback classname
     * </ol>
     *
     * @return Class object of factory, never null
     *
     * @param factoryId             Name of the factory to find, same as
     *                              a property name
     * @param propertiesFilename The filename in the $java.home/lib directory
     *                           of the properties file.  If none specified,
     *                           ${java.home}/lib/xerces.properties will be used.
     * @param fallbackClassName     Implementation class name, if nothing else
     *                              is found.  Use null to mean no fallback.
     *
     * @exception ObjectFactory.ConfigurationError
     */
    static Object createObject(String factoryId,
                                      String propertiesFilename,
                                      String fallbackClassName)
        throws ConfigurationError
    {
        if (DEBUG) debugPrintln("debug is on");

        SecuritySupport ss = SecuritySupport.getInstance();
        ClassLoader cl = findClassLoader();

        // Use the system property first
        try {
            String systemProp = ss.getSystemProperty(factoryId);
            if (systemProp != null) {
                if (DEBUG) debugPrintln("found system property, value=" + systemProp);
                return newInstance(systemProp, cl, true);
            }
        } catch (SecurityException se) {
            // Ignore and continue w/ next location
        }

        // Try to read from propertiesFilename, or $java.home/lib/xerces.properties
        String factoryClassName = null;
        // no properties file name specified; use $JAVA_HOME/lib/xerces.properties:
        if (propertiesFilename == null) {
            File propertiesFile = null;
            boolean propertiesFileExists = false;
            try {
                String javah = ss.getSystemProperty("java.home");
                propertiesFilename = javah + File.separator +
                    "lib" + File.separator + DEFAULT_PROPERTIES_FILENAME;
                propertiesFile = new File(propertiesFilename);
                propertiesFileExists = ss.getFileExists(propertiesFile);
            } catch (SecurityException e) {
                // try again...
                fLastModified = -1;
                fXercesProperties = null;
            }

            synchronized (ObjectFactory.class) {
                boolean loadProperties = false;
                try {
                    // file existed last time
                    if(fLastModified >= 0) {
                        if(propertiesFileExists &&
                                (fLastModified < (fLastModified = ss.getLastModified(propertiesFile)))) {
                            loadProperties = true;
                        } else {
                            // file has stopped existing...
                            if(!propertiesFileExists) {
                                fLastModified = -1;
                                fXercesProperties = null;
                            } // else, file wasn't modified!
                        }
                    } else {
                        // file has started to exist:
                        if(propertiesFileExists) {
                            loadProperties = true;
                            fLastModified = ss.getLastModified(propertiesFile);
                        } // else, nothing's changed
                    }
                    if(loadProperties) {
                        // must never have attempted to read xerces.properties before (or it's outdeated)
                        fXercesProperties = new Properties();
                        FileInputStream fis = ss.getFileInputStream(propertiesFile);
                        fXercesProperties.load(fis);
                        fis.close();
                    }
	            } catch (Exception x) {
	                fXercesProperties = null;
	                fLastModified = -1;
                    // assert(x instanceof FileNotFoundException
	                //        || x instanceof SecurityException)
	                // In both cases, ignore and continue w/ next location
	            }
            }
            if(fXercesProperties != null) {
                factoryClassName = fXercesProperties.getProperty(factoryId);
            }
        } else {
            try {
                FileInputStream fis = ss.getFileInputStream(new File(propertiesFilename));
                Properties props = new Properties();
                props.load(fis);
                fis.close();
                factoryClassName = props.getProperty(factoryId);
            } catch (Exception x) {
                // assert(x instanceof FileNotFoundException
                //        || x instanceof SecurityException)
                // In both cases, ignore and continue w/ next location
            }
        }
        if (factoryClassName != null) {
            if (DEBUG) debugPrintln("found in " + propertiesFilename + ", value=" + factoryClassName);
            return newInstance(factoryClassName, cl, true);
        }

        // Try Jar Service Provider Mechanism
        Object provider = findJarServiceProvider(factoryId);
        if (provider != null) {
            return provider;
        }

        if (fallbackClassName == null) {
            throw new ConfigurationError(
                "Provider for " + factoryId + " cannot be found", null);
        }

        if (DEBUG) debugPrintln("using fallback, value=" + fallbackClassName);
        return newInstance(fallbackClassName, cl, true);
    } // createObject(String,String,String):Object

    //
    // Private static methods
    //

    /** Prints a message to standard error if debugging is enabled. */
    private static void debugPrintln(String msg) {
        if (DEBUG) {
            System.err.println("JAXP: " + msg);
        }
    } // debugPrintln(String)

    /**
     * Figure out which ClassLoader to use.  For JDK 1.2 and later use
     * the context ClassLoader.
     */
    static ClassLoader findClassLoader()
        throws ConfigurationError
    {
        SecuritySupport ss = SecuritySupport.getInstance();

        // Figure out which ClassLoader to use for loading the provider
        // class.  If there is a Context ClassLoader then use it.
        ClassLoader context = ss.getContextClassLoader();
        ClassLoader system = ss.getSystemClassLoader();

        ClassLoader chain = system;
        while (true) {
            if (context == chain) {
                // Assert: we are on JDK 1.1 or we have no Context ClassLoader
                // or any Context ClassLoader in chain of system classloader
                // (including extension ClassLoader) so extend to widest
                // ClassLoader (always look in system ClassLoader if Xerces
                // is in boot/extension/system classpath and in current
                // ClassLoader otherwise); normal classloaders delegate
                // back to system ClassLoader first so this widening doesn't
                // change the fact that context ClassLoader will be consulted
                ClassLoader current = ObjectFactory.class.getClassLoader();

                chain = system;
                while (true) {
                    if (current == chain) {
                        // Assert: Current ClassLoader in chain of
                        // boot/extension/system ClassLoaders
                        return system;
                    }
                    if (chain == null) {
                        break;
                    }
                    chain = ss.getParentClassLoader(chain);
                }

                // Assert: Current ClassLoader not in chain of
                // boot/extension/system ClassLoaders
                return current;
            }

            if (chain == null) {
                // boot ClassLoader reached
                break;
            }

            // Check for any extension ClassLoaders in chain up to
            // boot ClassLoader
            chain = ss.getParentClassLoader(chain);
        };

        // Assert: Context ClassLoader not in chain of
        // boot/extension/system ClassLoaders
        return context;
    } // findClassLoader():ClassLoader

    /**
     * Create an instance of a class using the specified ClassLoader
     */
    static Object newInstance(String className, ClassLoader cl,
                                      boolean doFallback)
        throws ConfigurationError
    {
        // assert(className != null);
        try{
            Class providerClass = findProviderClass(className, cl, doFallback);
            Object instance = providerClass.newInstance();
            if (DEBUG) debugPrintln("created new instance of " + providerClass +
                   " using ClassLoader: " + cl);
            return instance;
        } catch (ClassNotFoundException x) {
            throw new ConfigurationError(
                "Provider " + className + " not found", x);
        } catch (Exception x) {
            throw new ConfigurationError(
                "Provider " + className + " could not be instantiated: " + x,
                x);
        }
    }

    /**
     * Find a Class using the specified ClassLoader
     */
    static Class findProviderClass(String className, ClassLoader cl,
                                      boolean doFallback)
        throws ClassNotFoundException, ConfigurationError
    {
        //throw security exception if the calling thread is not allowed to access the package
        //restrict the access to package as speicified in java.security policy
        SecurityManager security = System.getSecurityManager();
        try{
            if (security != null) {
                final int lastDot = className.lastIndexOf(".");
                String packageName = className;
                if (lastDot != -1) packageName = className.substring(0, lastDot);
                security.checkPackageAccess(packageName);
            }
        }catch(SecurityException e){
            throw e ;
        }
        Class providerClass;
        if (cl == null) {
            // XXX Use the bootstrap ClassLoader.  There is no way to
            // load a class using the bootstrap ClassLoader that works
            // in both JDK 1.1 and Java 2.  However, this should still
            // work b/c the following should be true:
            //
            // (cl == null) iff current ClassLoader == null
            //
            // Thus Class.forName(String) will use the current
            // ClassLoader which will be the bootstrap ClassLoader.
            providerClass = Class.forName(className);
        } else {
            try {
                providerClass = cl.loadClass(className);
            } catch (ClassNotFoundException x) {
                if (doFallback) {
                    // Fall back to current classloader
                    ClassLoader current = ObjectFactory.class.getClassLoader();
                    if (current == null) {
                        providerClass = Class.forName(className);
                    } else if (cl != current) {
                        cl = current;
                        providerClass = cl.loadClass(className);
                    } else {
                        throw x;
                    }
                } else {
                    throw x;
                }
            }
        }

        return providerClass;
    }

    /*
     * Try to find provider using Jar Service Provider Mechanism
     *
     * @return instance of provider class if found or null
     */
    private static Object findJarServiceProvider(String factoryId)
        throws ConfigurationError
    {
        SecuritySupport ss = SecuritySupport.getInstance();
        String serviceId = "META-INF/services/" + factoryId;
        InputStream is = null;

        // First try the Context ClassLoader
        ClassLoader cl = findClassLoader();

        is = ss.getResourceAsStream(cl, serviceId);

        // If no provider found then try the current ClassLoader
        if (is == null) {
            ClassLoader current = ObjectFactory.class.getClassLoader();
            if (cl != current) {
                cl = current;
                is = ss.getResourceAsStream(cl, serviceId);
            }
        }

        if (is == null) {
            // No provider found
            return null;
        }

        if (DEBUG) debugPrintln("found jar resource=" + serviceId +
               " using ClassLoader: " + cl);

        // Read the service provider name in UTF-8 as specified in
        // the jar spec.  Unfortunately this fails in Microsoft
        // VJ++, which does not implement the UTF-8
        // encoding. Theoretically, we should simply let it fail in
        // that case, since the JVM is obviously broken if it
        // doesn't support such a basic standard.  But since there
        // are still some users attempting to use VJ++ for
        // development, we have dropped in a fallback which makes a
        // second attempt using the platform's default encoding. In
        // VJ++ this is apparently ASCII, which is a subset of
        // UTF-8... and since the strings we'll be reading here are
        // also primarily limited to the 7-bit ASCII range (at
        // least, in English versions), this should work well
        // enough to keep us on the air until we're ready to
        // officially decommit from VJ++. [Edited comment from
        // jkesselm]
        BufferedReader rd;
        try {
            rd = new BufferedReader(new InputStreamReader(is, "UTF-8"), DEFAULT_LINE_LENGTH);
        } catch (java.io.UnsupportedEncodingException e) {
            rd = new BufferedReader(new InputStreamReader(is), DEFAULT_LINE_LENGTH);
        }

        String factoryClassName = null;
        try {
            // XXX Does not handle all possible input as specified by the
            // Jar Service Provider specification
            factoryClassName = rd.readLine();
            rd.close();
        } catch (IOException x) {
            // No provider found
            return null;
        }

        if (factoryClassName != null &&
            ! "".equals(factoryClassName)) {
            if (DEBUG) debugPrintln("found in resource, value="
                   + factoryClassName);

            // Note: here we do not want to fall back to the current
            // ClassLoader because we want to avoid the case where the
            // resource file was found using one ClassLoader and the
            // provider class was instantiated using a different one.
            return newInstance(factoryClassName, cl, false);
        }

        // No provider found
        return null;
    }

    //
    // Classes
    //

    /**
     * A configuration error.
     */
    static class ConfigurationError
        extends Error {

        //
        // Data
        //

        /** Exception. */
        private Exception exception;

        //
        // Constructors
        //

        /**
         * Construct a new instance with the specified detail string and
         * exception.
         */
        ConfigurationError(String msg, Exception x) {
            super(msg);
            this.exception = x;
        } // <init>(String,Exception)

        //
        // methods
        //

        /** Returns the exception associated to this error. */
        Exception getException() {
            return exception;
        } // getException():Exception

    } // class ConfigurationError

} // class ObjectFactory
