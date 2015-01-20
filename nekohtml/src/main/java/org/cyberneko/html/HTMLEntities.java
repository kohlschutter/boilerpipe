/* 
 * Copyright 2002-2009 Andy Clark, Marc Guillemot
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cyberneko.html;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
                           
/**
 * Pre-defined HTML entities.
 *
 * @author Andy Clark
 *
 * @version $Id: HTMLEntities.java,v 1.5 2005/02/14 03:56:54 andyc Exp $
 */
public class HTMLEntities {

    //
    // Constants
    //

    /** Entities. */
    protected static final Map ENTITIES;

    /** Reverse mapping from characters to names. */
    protected static final IntProperties SEITITNE = new IntProperties();

    //
    // Static initialization
    //

    static {
    	final Properties props = new Properties();
        // load entities
        load0(props, "res/HTMLlat1.properties");
        load0(props, "res/HTMLspecial.properties");
        load0(props, "res/HTMLsymbol.properties");
        load0(props, "res/XMLbuiltin.properties");

        // store reverse mappings
        final Enumeration keys = props.propertyNames();
        while (keys.hasMoreElements()) {
        	final String key = (String)keys.nextElement();
        	final String value = props.getProperty(key);
            if (value.length() == 1) {
            	final int ivalue = value.charAt(0);
                SEITITNE.put(ivalue, key);
            }
        }
        
        ENTITIES = Collections.unmodifiableMap(new HashMap(props));
    }

    //
    // Public static methods
    //

    /** 
     * Returns the character associated to the given entity name, or
     * -1 if the name is not known.
     */
    public static int get(String name) {
        String value = (String)ENTITIES.get(name);
        return value != null ? value.charAt(0) : -1;
    } // get(String):char

    /** 
     * Returns the name associated to the given character or null if
     * the character is not known.
     */
    public static String get(int c) {
        return SEITITNE.get(c);
    } // get(int):String

    //
    // Private static methods
    //

    /** Loads the entity values in the specified resource. */
    private static void load0(final Properties props, final String filename) {
        try {
        	final InputStream stream = HTMLEntities.class.getResourceAsStream(filename);
			props.load(stream);
			stream.close();
        }
        catch (final IOException e) {
            System.err.println("error: unable to load resource \""+filename+"\"");
        }
    } // load0(String)

    //
    // Classes
    //

    static class IntProperties {
        private Entry[] entries = new Entry[101];
        public void put(int key, String value) {
            int hash = key % entries.length;
            Entry entry = new Entry(key, value, entries[hash]);
            entries[hash] = entry;
        }
        public String get(int key) {
            int hash = key % entries.length;
            Entry entry = entries[hash];
            while (entry != null) {
                if (entry.key == key) {
                    return entry.value;
                }
                entry = entry.next;
            }
            return null;
        }
        static class Entry {
            public int key;
            public String value;
            public Entry next;
            public Entry(int key, String value, Entry next) {
                this.key = key;
                this.value = value;
                this.next = next;
            }
        }
    }

} // class HTMLEntities
