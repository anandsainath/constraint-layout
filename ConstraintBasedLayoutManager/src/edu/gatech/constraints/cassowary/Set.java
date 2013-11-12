// $Id: Set.java,v 1.1 2008/08/12 22:32:43 larrymelia Exp $
//
// Cassowary Incremental Constraint Solver
// Original Smalltalk Implementation by Alan Borning
// This Java Implementation by Greg J. Badros, <gjb@cs.washington.edu>
// http://www.cs.washington.edu/homes/gjb
// (C) 1998, 1999 Greg J. Badros and Alan Borning
// See ../LICENSE for legal details regarding this software
//
// Set
// Encapsulate a mathematical "Set" ADT using java's
// hash table.  Just a convenience wrapper of the java.util.Hashtable class.

package edu.gatech.constraints.cassowary;

import java.util.*;

class Set {
    public Set() {
        hash = new Hashtable<Object, Object>();
    }

    public Set(int i) {
        hash = new Hashtable<Object, Object>(i);
    }

    public Set(int i, float f) {
        hash = new Hashtable<Object, Object>(i, f);
    }

    public Set(Hashtable<Object, Object> h) {
        hash = h;
    }

    public boolean containsKey(Object o) {
        return hash.containsKey(o);
    }

    public boolean insert(Object o) {
        return hash.put(o, o) == null ? true : false;
    }

    public boolean remove(Object o) {
        return hash.remove(o) == null ? true : false;
    }

    public void clear() {
        hash.clear();
    }

    public int size() {
        return hash.size();
    }

    public boolean isEmpty() {
        return hash.isEmpty();
    }

    @SuppressWarnings("unchecked")
    public Object clone() {
        return new Set((Hashtable<Object, Object>) hash.clone());
    }

    public Enumeration<Object> elements() {
        return hash.elements();
    }

    public String toString() {
        StringBuffer bstr = new StringBuffer("{ ");
        Enumeration<Object> e = hash.keys();
        if (e.hasMoreElements())
            bstr.append(e.nextElement().toString());
        while (e.hasMoreElements()) {
            bstr.append(", " + e.nextElement());
        }
        bstr.append(" }\n");
        return bstr.toString();
    }

    private Hashtable<Object, Object> hash;
}
