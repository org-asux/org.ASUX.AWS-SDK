/*
 BSD 3-Clause License
 
 Copyright (c) 2019, Udaybhaskar Sarma Seetamraju
 All rights reserved.
 
 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 
 * Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.
 
 * Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.
 
 * Neither the name of the copyright holder nor the names of its
 contributors may be used to endorse or promote products derived from
 this software without specific prior written permission.
 
 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.ASUX.AWSSDK;

import org.ASUX.yaml.YAML_Libraries;
import org.ASUX.yaml.YAMLImplementation;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * <p> This org.ASUX.yaml GitHub.com project and the <a href="https://github.com/org-asux/org.ASUX.cmdline">org.ASUX.cmdline</a> GitHub.com projects.
 * </p>
 * <p> This is a class independent from most of  org.ASUX.yaml and org.ASUX.YAML.NodeImpl projects</p>
 * <p> This class is focuesed on providing a SnakeYAML's Node-based interface to specific AWS SDK APIs</p>
 * <p> The 3 API currently supported are: <b>list-regions, list-AZs, describe-AZs</b>. </p>
 * <p> See full details of how to use these commands - in this GitHub project's wiki at:<br>
 *  <a href="https://github.com/org-asux/org-ASUX.github.io/wiki">org.ASUX Wiki </a> on GitHub</p>
 */
public class Cmd {

    public static final String CLASSNAME = Cmd.class.getName();

    //=================================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //=================================================================================

    /**
     * This is NOT testing code. It's actual means by which user's command line arguments are read and processed
     * @param args user's commandline arguments
     */
    public static void main( String[] args )
    {
        final String HDR = CLASSNAME + ": main(String[]): ";
        CmdLineArgsAWS cmdlineargs = null;

        try {
            cmdlineargs = CmdLineArgsAWS.create( args );

            // Step 1: create 'cmdinvoker'
            final CmdInvoker cmdinvoker = new CmdInvoker( cmdlineargs.verbose, cmdlineargs.showStats );
            if (cmdlineargs.verbose) System.out.println( HDR +"getting started with cmdline args = " + cmdlineargs + " " );

            // Steps 2 & 3: Startup the factory for YAML-implementation.
            // For other projects in the org.ASUX family, especially those that do NOT want to know the YAML-implementation.. use the following line instead.
            final YAMLImplementation<?> yamlImpl = org.ASUX.yaml.YAMLImplementation.startupYAMLImplementationFactory( cmdlineargs.getYAMLLibrary(), cmdlineargs, cmdinvoker );
            // final YAMLImplementation<?> yamlImpl = org.ASUX.yaml.YAMLImplementation.startupYAMLImplementationFactory( YAML_Libraries.SNAKEYAML_Library, cmdlineargs, cmdinvoker );
            if (cmdlineargs.verbose) System.out.println( HDR +" set YAML-Library to [" + cmdlineargs.getYAMLLibrary() + " and [" + cmdinvoker.getYAMLLibrary() + "]" );

            //======================================================================
            // Step 4 on.. start processing...

            // run the command requested by user
            final Object output = cmdinvoker.processCommand( cmdlineargs, null );
            if (cmdlineargs.verbose) System.out.println( HDR +" processing of entire command returned [" + (output==null?"null":output.getClass().getName()) + "]" );

            //======================================================================
            final java.io.StringWriter javawriter = new java.io.StringWriter();
            yamlImpl.write( javawriter, output );

            //======================================================================
            yamlImpl.close(); // Yes! Even for stdout/System.out .. we need to call close(). This is driven by one the YAML libraries (eso teric soft ware)
            javawriter.flush();

            // Now since we have a surrogate for STDOUT for use by , let's dump its output onto STDOUT!
            if (cmdlineargs.verbose) System.out.println( HDR +" dumping the final output to STDOUT" );
            String outputStr = javawriter.toString();
            System.out.println( outputStr );

        // } catch (java.io.FileNotFoundException e) {
        //     if ( cmdlineargs == null || cmdlineargs.verbose ) e.printStackTrace(System.err);
        //     System.err.println( "INPUT-File Not found: '" + cmdlineargs.inputFilePath + "'\nCmdline arguments provided are: " + cmdlineargs + "\n"+ e );
        //     System.exit(8);
        } catch (java.io.IOException e) {
            if ( cmdlineargs == null || cmdlineargs.verbose ) e.printStackTrace(System.err);
            System.err.println( "java.io.IOException - Cmdline arguments provided are: " + cmdlineargs + "\nn"+ e );
            System.exit(7);
        } catch (Exception e) {
            if ( cmdlineargs == null || cmdlineargs.verbose ) e.printStackTrace(System.err);
            System.err.println( "Internal error: Cmdline arguments provided are: " + cmdlineargs + "\n"+ e );
            System.exit(6);
        }

    }

}
