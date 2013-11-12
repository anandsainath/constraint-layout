// $Id: ClTableau.java,v 1.1 2008/08/12 22:32:43 larrymelia Exp $
//
// Cassowary Incremental Constraint Solver
// Original Smalltalk Implementation by Alan Borning
// This Java Implementation by Greg J. Badros, <gjb@cs.washington.edu>
// http://www.cs.washington.edu/homes/gjb
// (C) 1998, 1999 Greg J. Badros and Alan Borning
// See ../LICENSE for legal details regarding this software
//
// ClTableau

package edu.gatech.constraints.cassowary;

import java.util.*;

class ClTableau extends CL {
    // ctr is protected, since this only supports an ADT for
    // the ClSimplexSolved class
    protected ClTableau() {
        _columns = new Hashtable<ClAbstractVariable, Set>();
        _rows = new Hashtable<ClAbstractVariable, ClLinearExpression>();
        _infeasibleRows = new Set();
        _externalRows = new Set();
        _externalParametricVars = new Set();
    }

    // Variable v has been removed from an expression. If the
    // expression is in a tableau the corresponding basic variable is
    // subject (or if subject is nil then it's in the objective function).
    // Update the column cross-indices.
    public final void noteRemovedVariable(ClAbstractVariable v,
            ClAbstractVariable subject) {
        if (fTraceOn)
            fnenterprint("noteRemovedVariable: " + v + ", " + subject);
        if (subject != null) {
            _columns.get(v).remove(subject);
        }
    }

    // v has been added to the linear expression for subject
    // update column cross indices
    public final void noteAddedVariable(ClAbstractVariable v,
            ClAbstractVariable subject) {
        if (fTraceOn)
            fnenterprint("noteAddedVariable: " + v + ", " + subject);
        if (subject != null) {
            insertColVar(v, subject);
        }
    }

    // Originally from Michael Noth <noth@cs>
    public String getInternalInfo() {
        StringBuffer retstr = new StringBuffer("Tableau Information:\n");
        retstr.append("Rows: " + _rows.size());
        retstr.append(" (= " + (_rows.size() - 1) + " constraints)");
        retstr.append("\nColumns: " + _columns.size());
        retstr.append("\nInfeasible Rows: " + _infeasibleRows.size());
        retstr.append("\nExternal basic variables: " + _externalRows.size());
        retstr.append("\nExternal parametric variables: ");
        retstr.append(_externalParametricVars.size());
        retstr.append("\n");

        return retstr.toString();
    }

    public String toString() {
        StringBuffer bstr = new StringBuffer("Tableau:\n");
        for (Enumeration<ClAbstractVariable> e = _rows.keys(); e
                .hasMoreElements();) {
            ClAbstractVariable clv = e.nextElement();
            ClLinearExpression expr = _rows.get(clv);
            bstr.append(clv.toString());
            bstr.append(" <==> ");
            bstr.append(expr.toString());
            bstr.append("\n");
        }

        bstr.append("\nColumns:\n");
        bstr.append(_columns.toString());

        bstr.append("\nInfeasible rows: ");
        bstr.append(_infeasibleRows.toString());

        bstr.append("External basic variables: ");
        bstr.append(_externalRows.toString());

        bstr.append("External parametric variables: ");
        bstr.append(_externalParametricVars.toString());

        return bstr.toString();
    }

    // Convenience function to insert a variable into
    // the set of rows stored at _columns[param_var],
    // creating a new set if needed
    private final void insertColVar(ClAbstractVariable param_var,
            ClAbstractVariable rowvar) {
        Set rowset = _columns.get(param_var);
        if (rowset == null)
            _columns.put(param_var, rowset = new Set());
        rowset.insert(rowvar);
    }

    // Add v=expr to the tableau, update column cross indices
    // v becomes a basic variable
    // expr is now owned by ClTableau class,
    // and ClTableauis responsible for deleting it
    // (also, expr better be allocated on the heap!)
    protected final void addRow(ClAbstractVariable var, ClLinearExpression expr) {
        if (fTraceOn)
            fnenterprint("addRow: " + var + ", " + expr);

        // for each variable in expr, add var to the set of rows which
        // have that variable in their expression
        _rows.put(var, expr);

        for (Enumeration<ClAbstractVariable> e = expr.terms().keys(); e
                .hasMoreElements();) {
            ClAbstractVariable clv = e.nextElement();
            insertColVar(clv, var);
            if (clv.isExternal()) {
                _externalParametricVars.insert(clv);
            }
        }
        if (var.isExternal()) {
            _externalRows.insert(var);
        }
        if (fTraceOn)
            traceprint(this.toString());
    }

    // Remove v from the tableau -- remove the column cross indices for v
    // and remove v from every expression in rows in which v occurs
    protected final void removeColumn(ClAbstractVariable var) {
        if (fTraceOn)
            fnenterprint("removeColumn:" + var);
        // remove the rows with the variables in varset

        Set rows = _columns.remove(var);

        if (rows != null) {
            for (Enumeration<Object> e = rows.elements(); e.hasMoreElements();) {
                ClAbstractVariable clv = (ClAbstractVariable) e.nextElement();
                ClLinearExpression expr = _rows.get(clv);
                expr.terms().remove(var);
            }
        } else {
            if (fTraceOn)
                debugprint("Could not find var " + var + " in _columns");
        }

        if (var.isExternal()) {
            _externalRows.remove(var);
            _externalParametricVars.remove(var);
        }
    }

    // Remove the basic variable v from the tableau row v=expr
    // Then update column cross indices
    protected final ClLinearExpression removeRow(ClAbstractVariable var)
            throws ExCLInternalError {
        if (fTraceOn)
            fnenterprint("removeRow:" + var);

        ClLinearExpression expr = _rows.get(var);
        assert (expr != null);

        // For each variable in this expression, update
        // the column mapping and remove the variable from the list
        // of rows it is known to be in
        for (Enumeration<ClAbstractVariable> e = expr.terms().keys(); e
                .hasMoreElements();) {
            ClAbstractVariable clv = e.nextElement();
            Set varset = _columns.get(clv);
            if (varset != null) {
                if (fTraceOn)
                    debugprint("removing from varset " + var);
                varset.remove(var);
            }
        }

        _infeasibleRows.remove(var);

        if (var.isExternal()) {
            _externalRows.remove(var);
        }
        _rows.remove(var);
        if (fTraceOn)
            fnexitprint("returning " + expr);
        return expr;
    }

    // Replace all occurrences of oldVar with expr, and update column cross
    // indices
    // oldVar should now be a basic variable
    protected final void substituteOut(ClAbstractVariable oldVar,
            ClLinearExpression expr) {
        if (fTraceOn)
            fnenterprint("substituteOut:" + oldVar + ", " + expr);
        if (fTraceOn)
            traceprint(this.toString());

        Set varset = _columns.get(oldVar);
        for (Enumeration<Object> e = varset.elements(); e.hasMoreElements();) {
            ClAbstractVariable v = (ClAbstractVariable) e.nextElement();
            ClLinearExpression row = _rows.get(v);
            row.substituteOut(oldVar, expr, v, this);
            if (v.isRestricted() && row.constant() < 0.0) {
                _infeasibleRows.insert(v);
            }
        }

        if (oldVar.isExternal()) {
            _externalRows.insert(oldVar);
            _externalParametricVars.remove(oldVar);
        }
        _columns.remove(oldVar);
    }

    protected final Hashtable<ClAbstractVariable, Set> columns() {
        return _columns;
    }

    protected final Hashtable<ClAbstractVariable, ClLinearExpression> rows() {
        return _rows;
    }

    // return true iff the variable subject is in the columns keys
    protected final boolean columnsHasKey(ClAbstractVariable subject) {
        return _columns.containsKey(subject);
    }

    protected final ClLinearExpression rowExpression(ClAbstractVariable v) {
        // if (fTraceOn) fnenterprint("rowExpression:" + v);
        return _rows.get(v);
    }

    // _columns is a mapping from variables which occur in expressions to the
    // set of basic variables whose expressions contain them
    // i.e., it's a mapping from variables in expressions (a column) to the
    // set of rows that contain them
    protected Hashtable<ClAbstractVariable, Set> _columns; // From
    // ClAbstractVariable
    // to Set of
    // variables

    // _rows maps basic variables to the expressions for that row in the tableau
    protected Hashtable<ClAbstractVariable, ClLinearExpression> _rows; // From
    // ClAbstractVariable
    // to
    // ClLinearExpression

    // the collection of basic variables that have infeasible rows
    // (used when reoptimizing)
    protected Set _infeasibleRows; // Set
    // of
    // ClAbstractVariable-s

    // the set of rows where the basic variable is external
    // this was added to the Java/C++ versions to reduce time in
    // setExternalVariables()
    protected Set _externalRows; // Set
    // of
    // ClVariable-s

    // the set of external variables which are parametric
    // this was added to the Java/C++ versions to reduce time in
    // setExternalVariables()
    protected Set _externalParametricVars; // Set
    // of
    // ClVariable-s

}
