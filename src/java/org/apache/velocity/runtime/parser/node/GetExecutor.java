package org.apache.velocity.runtime.parser.node;
/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Velocity", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

import java.lang.reflect.Method;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.util.introspection.Introspector;

import java.lang.reflect.InvocationTargetException;
import org.apache.velocity.exception.MethodInvocationException;

import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeLogger;


/**
 * Executor that simply tries to execute a get(key)
 * operation. This will try to find a get(key) method
 * for any type of object, not just objects that
 * implement the Map interface as was previously
 * the case.
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @version $Id: GetExecutor.java,v 1.7 2002/04/21 20:57:25 geirm Exp $
 */
public class GetExecutor extends AbstractExecutor
{
    /**
     * Container to hold the 'key' part of 
     * get(key).
     */
    private Object[] args = new Object[1];
    
    /**
     * Default constructor.
     */
    public GetExecutor(RuntimeLogger r, Introspector ispect, Class c, String key)
        throws Exception
    {
        rlog = r;
        args[0] = key;
        method = ispect.getMethod(c, "get", args);
    }

    /**
     * Execute method against context.
     */
    public Object execute(Object o)
        throws IllegalAccessException, InvocationTargetException
    {
        if (method == null)
            return null;

        return method.invoke(o, args);
    }

    /**
     * Execute method against context.
     */
    public Object OLDexecute(Object o, InternalContextAdapter context)
        throws IllegalAccessException, MethodInvocationException
    {
        if (method == null)
            return null;
     
        try 
        {
            return method.invoke(o, args);  
        }
        catch(InvocationTargetException ite)
        {
            /*
             *  the method we invoked threw an exception.
             *  package and pass it up
             */

            throw  new MethodInvocationException( 
                "Invocation of method 'get(\"" + args[0] + "\")'" 
                + " in  " + o.getClass() 
                + " threw exception " 
                + ite.getTargetException().getClass(), 
                ite.getTargetException(), "get");
        }
        catch(IllegalArgumentException iae)
        {
            return null;
        }
    }
}









