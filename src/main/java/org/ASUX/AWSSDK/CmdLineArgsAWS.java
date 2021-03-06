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

import java.util.ArrayList;

import org.ASUX.yaml.CmdLineArgsCommon;
import org.apache.commons.cli.*;

import static org.junit.Assert.*;

/** <p>This class is a typical use of the org.apache.commons.cli package.</p>
 *  <p>This class has No other function - other than to parse the commandline arguments and handle user's input errors.</p>
 *  <p>For making it easy to have simple code generate debugging-output, added a toString() method to this class.</p>
 *  <p>Typical use of this class is: </p>
 *<pre>
 public static void main(String[] args) {
 cmdLineArgs = new CmdLineArgsAWS(args);
 .. ..
 *</pre>
 *
 *  <p>See full details of how to use this, in {@link org.ASUX.yaml.Cmd} as well as the <a href="https://github.com/org-asux/org.ASUX.cmdline">org.ASUX.cmdline</a> GitHub.com project.</p>
 * @see org.ASUX.yaml.Cmd
 */
public class CmdLineArgsAWS extends org.ASUX.yaml.CmdLineArgsCommon {

    private static final long serialVersionUID = 412L;

    public static final String CLASSNAME = CmdLineArgsAWS.class.getName();

    public static final String[] LISTREGIONS = { "lr", "list-regions", "list all the AWS regions" };
    public static final String[] GETVPCID = { "vid", "get-vpc-id", "get the ID of the 1st/default VPC in a specified region" };
    public static final String[] DESCRIBEVPCS = { "vpcs", "describe-vpcs", "describe all details (incl. tags) of ALL VPCs in a specified region" };
    public static final String[] LISTAZS = { "laz", "list-AZs", "List all the AZs in a specified region" };
    public static final String[] DESCRIBEAZS = { "dz", "describe-AZs", "describe all the AZs in a specified region" };

    public static final String[] S364HEX_CANONICALID = { "s3hex", "s3-canonicalid-64char-hex", "get *MY* 64-character long all-hexadecimal S3-Canonical-ID, to compare with owner-id listed in S3 access-rights" };

    public static final String[] CREATEKEYPAIR = { "ck", "create-key-pair", "create a new keypair within a specified region, with the provided-name of the keypair" };
    public static final String[] DELETEKEYPAIR = { "dk", "delete-key-pair", "delete an existing keypair in a specified region and the name of the keypair" };
    public static final String[] LISTKEYPAIR = { "lk", "describe-key-pairs", "show all keypair within a specified region, matching the provided-name of the keypair" };

    //------------------------------------
    private Enums.SDKCommands cmdType = Enums.SDKCommands.Undefined;
    private String AWSRegion = "uninitialized-AWSRegion";
    private String SubParameter1 = "uninitialized-SubParameter1"; // Theoretically, we can have commands that do NOT have AWSRegion as SubParameter1
    private String SubParameter2 = "uninitialized-SubParameter2";

    //=================================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //=================================================================================

    /**
     *  <p>Add cmd-line argument definitions (using apache.commons.cli.Options) for the instance-variables defined in this class.</p>
     */
    @Override
    protected void defineAdditionalOptions()
    {
        OptionGroup grp = new OptionGroup();
        Option opt;

        opt = new Option( LISTREGIONS[0], LISTREGIONS[1], false, LISTREGIONS[2] );
        grp.addOption( opt );

        opt = CmdLineArgsCommon.genOption( GETVPCID[0], GETVPCID[1], GETVPCID[2], 1, "region" );
        grp.addOption( opt );
        opt = CmdLineArgsCommon.genOption( DESCRIBEVPCS[0], DESCRIBEVPCS[1], DESCRIBEVPCS[2], 1, "region" );
        grp.addOption( opt );
        opt = CmdLineArgsCommon.genOption( LISTAZS[0], LISTAZS[1], LISTAZS[2], 1, "region" );
        grp.addOption( opt );
        opt = CmdLineArgsCommon.genOption( DESCRIBEAZS[0], DESCRIBEAZS[1], DESCRIBEAZS[2], 1, "region" );
        grp.addOption( opt );

        opt = CmdLineArgsCommon.genOption( S364HEX_CANONICALID[0], S364HEX_CANONICALID[1], S364HEX_CANONICALID[2], 1, "region" );
        grp.addOption( opt );

        opt = CmdLineArgsCommon.genOption( CREATEKEYPAIR[0], CREATEKEYPAIR[1], CREATEKEYPAIR[2], 2, "region> <New-SSHKeyPair-Name" );
        grp.addOption( opt );
        opt = CmdLineArgsCommon.genOption( DELETEKEYPAIR[0], DELETEKEYPAIR[1], DELETEKEYPAIR[2], 2, "region> <ExistingSSHKeyPair-Name" );
        grp.addOption( opt );
        opt = CmdLineArgsCommon.genOption( LISTKEYPAIR[0], LISTKEYPAIR[1], LISTKEYPAIR[2], 2, "region> <ExistingSSHKeyPair-Name" );
        grp.addOption( opt );

        grp.setRequired(true);

        this.options.addOptionGroup(grp);
    }

    //=================================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //=================================================================================

    //=================================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //=================================================================================

    /**
     *  @see org.ASUX.yaml.CmdLineArgsCommon#parseAdditionalOptions
     */
    @Override
    protected void parseAdditionalOptions( String[] _args, final org.apache.commons.cli.CommandLine _apacheCmdProcessor )
                    throws MissingOptionException, ParseException, Exception
    {
        final String HDR = CLASSNAME + ": parseCmdLineForNewOptions([]],..): ";

        if ( _apacheCmdProcessor.hasOption(LISTREGIONS[1]) ) {
            this.cmdType = Enums.SDKCommands.listRegions;
        }
        if ( _apacheCmdProcessor.hasOption(GETVPCID[1]) ) {
            this.cmdType = Enums.SDKCommands.getVPCID;
            this.AWSRegion = _apacheCmdProcessor.getOptionValue( GETVPCID[1] );
        }
        if ( _apacheCmdProcessor.hasOption(DESCRIBEVPCS[1]) ) {
            this.cmdType = Enums.SDKCommands.describeVPCs;
            this.AWSRegion = _apacheCmdProcessor.getOptionValue( DESCRIBEVPCS[1] );
        }
        if ( _apacheCmdProcessor.hasOption(LISTAZS[1]) ) {
            this.cmdType = Enums.SDKCommands.listAZs;
            this.AWSRegion = _apacheCmdProcessor.getOptionValue( LISTAZS[1] );
        }
        if ( _apacheCmdProcessor.hasOption(DESCRIBEAZS[1]) ) {
            this.cmdType = Enums.SDKCommands.describeAZs;
            this.AWSRegion = _apacheCmdProcessor.getOptionValue( DESCRIBEAZS[1] );
        }

        if ( _apacheCmdProcessor.hasOption(S364HEX_CANONICALID[1]) ) {
            this.cmdType = Enums.SDKCommands.s364HexCanonicalId;
            this.AWSRegion = _apacheCmdProcessor.getOptionValue( S364HEX_CANONICALID[1] );
        }

        if ( _apacheCmdProcessor.hasOption(CREATEKEYPAIR[1]) ) {
            this.cmdType = Enums.SDKCommands.createKeyPair;
            final String[] subParams = _apacheCmdProcessor.getOptionValues( CREATEKEYPAIR[1] ); // CmdLineArgsBasic.INSERTCMD[1] );
            // because we set .setArgs(2) in define().. you can get the values for:- subParams[0] and subParams[1].
            this.AWSRegion = subParams[0]; // 1st of the 2 arguments for INSERT cmd.
            this.SubParameter1 = subParams[0]; // 1st of the 2 arguments for INSERT cmd.
            this.SubParameter2 = subParams[1];
        }
        if ( _apacheCmdProcessor.hasOption(DELETEKEYPAIR[1]) ) {
            this.cmdType = Enums.SDKCommands.deleteKeyPair;
            final String[] subParams = _apacheCmdProcessor.getOptionValues( DELETEKEYPAIR[1] ); // CmdLineArgsBasic.INSERTCMD[1] );
            // because we set .setArgs(2) in define().. you can get the values for:- subParams[0] and subParams[1].
            this.AWSRegion = subParams[0]; // 1st of the 2 arguments for INSERT cmd.
            this.SubParameter1 = subParams[0]; // 1st of the 2 arguments for INSERT cmd.
            this.SubParameter2 = subParams[1];
        }
        if ( _apacheCmdProcessor.hasOption(LISTKEYPAIR[1]) ) {
            this.cmdType = Enums.SDKCommands.listKeyPairs;
            final String[] subParams = _apacheCmdProcessor.getOptionValues( LISTKEYPAIR[1] ); // CmdLineArgsBasic.INSERTCMD[1] );
            // because we set .setArgs(2) in define().. you can get the values for:- subParams[0] and subParams[1].
            this.AWSRegion = subParams[0]; // 1st of the 2 arguments for INSERT cmd.
            this.SubParameter1 = subParams[0]; // 1st of the 2 arguments for INSERT cmd.
            this.SubParameter2 = subParams[1];
        }

        assertTrue( this.cmdType != Enums.SDKCommands.Undefined );
    }

    //=================================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //=================================================================================

    /** For making it easy to have simple code generate debugging-output, added this toString() method to this class.
     */
    @Override
    public String toString() {
        return
        super.toString()
        +" cmdType="+cmdType +" AWSRegion="+ this.AWSRegion +" SubParameter1="+ this.SubParameter1 +" SubParameter2="+ this.SubParameter2
        ;
    }

    public Enums.SDKCommands getCmdType()   { return this.cmdType; }
    public String getAWSRegion()            { return this.AWSRegion; }
    public String getSubParameter1()        { return this.SubParameter1; }
    public String getSubParameter2()        { return this.SubParameter2; }

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

    /**
     * This returns an object of this class/type, but as a pointer to the parent class (for use in a generic way, by org.ASUX.yaml.BatchCmdProcessor.java)
     * @param args the command-line args as passed to main()
     * @return a non-null instance of this class, but as a reference of parent/super-class.
     * @throws Exception if any issue parsing or validating the cmdline arguments passed to this method
     */
    public static final org.ASUX.AWSSDK.CmdLineArgsAWS create( final String[] args )
                                        throws Exception
    {
        final String HDR = CLASSNAME + ": create("+ args.length +"): ";
        CmdLineArgsAWS cmdlineargs = null;
        // if ( true ) { System.out.print( HDR +": ");  for( String s: args) System.out.print(s+"\t"); System.out.println(); }

        try {
            cmdlineargs = new CmdLineArgsAWS();
            cmdlineargs.define();
            cmdlineargs.parse( args );
            // Until we get past the above statement, we don't know about 'verbose'
            if (cmdlineargs.verbose) { System.out.print( HDR +" >>>>>>>>>>>>> "); for( String s: cmdlineargs.getArgs()) System.out.print(s);  System.out.println(); }
            if (cmdlineargs.verbose) System.out.println( HDR +" cmdlineargs=["+ cmdlineargs +"]" );

            return cmdlineargs;

        } catch (Exception e) {
            if ( cmdlineargs == null || cmdlineargs.verbose ) e.printStackTrace(System.err);
            if ( cmdlineargs == null || cmdlineargs.verbose ) System.err.println( "Internal ERROR: Cmdline arguments provided are: " + cmdlineargs + "\n"+ e );
            throw(e);
        }
    }

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

    // For unit-testing purposes only
//    public static void main(String[] args) {
//        new CmdLineArgsAWS(args);
//    }

}
