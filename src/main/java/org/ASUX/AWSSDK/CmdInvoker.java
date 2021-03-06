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

import org.ASUX.yaml.JSONTools;
import org.ASUX.YAML.NodeImpl.NodeTools;

import org.ASUX.yaml.YAML_Libraries;
import org.ASUX.yaml.MemoryAndContext;
import org.ASUX.yaml.YAMLImplementation;
import org.ASUX.YAML.NodeImpl.NodeTools;
import org.ASUX.yaml.CmdLineArgs;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

// https://yaml.org/spec/1.2/spec.html#id2762107
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.error.Mark; // https://bitbucket.org/asomov/snakeyaml/src/default/src/main/java/org/yaml/snakeyaml/error/Mark.java

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * <p> This org.ASUX.yaml GitHub.com project and the <a href="https://github.com/org-asux/org.ASUX.cmdline">org.ASUX.cmdline</a> GitHub.com projects.</p>
 * <p>This class and entire library is tied to the SnakeYAML implementation (org.ASUX.YAML.NodeImpl).  This is because only this YAML-Implementation library can handle !Ref and !Condition and other CFN tags.</p>
 * <p> This is a class independent from most of  org.ASUX.yaml and org.ASUX.YAML.NodeImpl projects</p>
 * <p> This class is focuesed on providing a SnakeYAML's Node-based interface to specific AWS SDK APIs</p>
 * <p> The 3 API currently supported are: <b>list-regions, list-AZs, describe-AZs</b>. </p>
 * <p> See full details of how to use these commands - in this GitHub project's wiki at:<br>
 *  <a href="https://github.com/org-asux/org-ASUX.github.io/wiki">org.ASUX Wiki </a> on GitHub</p>
 */

public class CmdInvoker extends org.ASUX.yaml.CmdInvoker {

    private static final long serialVersionUID = 602L;

    public static final String CLASSNAME = CmdInvoker.class.getName();

    private CmdLineArgsAWS cmdlineargsaws;

    //=================================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //=================================================================================

    /**
     *  The constructor exclusively for use by  main() classes anywhere.
     *  @param _verbose Whether you want deluge of debug-output onto System.out.
     *  @param _showStats Whether you want a final summary onto console / System.out
     */
    public CmdInvoker( final boolean _verbose, final boolean _showStats ) {
        super( _verbose, _showStats );
    }

    /**
     *  Variation of constructor that allows you to pass-in memory from another previously existing instance of this class.  Useful within org.ASUX.YAML.NodeImp.BatchYamlProcessor which creates new instances of this class, whenever it encounters a YAML or AWS command within the Batch-file.
     *  @param _verbose Whether you want deluge of debug-output onto System.out.
     *  @param _showStats Whether you want a final summary onto console / System.out
     *  @param _memoryAndContext pass in memory from another previously existing instance of this class.  Useful within org.ASUX.YAML.CollectionImpl.BatchYamlProcessor which creates new instances of this class, whenever it encounters a YAML or AWS command within the Batch-file.
     */
    public CmdInvoker( final boolean _verbose, final boolean _showStats, final MemoryAndContext _memoryAndContext ) {
        super(_verbose, _showStats, _memoryAndContext );
    }

    //=================================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //=================================================================================
    // /**
    //  *  This function is meant to be used by Cmd.main() and by BatchProcessor.java.  Invoke this method *BEFORE* invoking {@link #processCommand(org.ASUX.yaml.CmdLineArgsCommon, Object)}.
    //  *  @param _cmdLineArgs Everything passed as commandline arguments to the Java program {@link org.ASUX.yaml.CmdLineArgsCommon}
    //  *  @throws Exception None for now.
    //  */
    // @Override
    // public org.ASUX.yaml.YAMLImplementation<?> config( org.ASUX.yaml.CmdLineArgsCommon _cmdLineArgs ) throws Exception
    // {   final String HDR = CLASSNAME + ": config("+ _cmdLineArgs +"): ";
    //     // assertTrue( _cmdLineArgs instanceof org.ASUX.yaml.CmdLineArgs );
    //     // final org.ASUX.yaml.CmdLineArgs cmdLineArgs = (org.ASUX.yaml.CmdLineArgs) _cmdLineArgs;
    //     // final String HDR = CLASSNAME + ": config("+ cmdLineArgs.cmdType +"): ";
    //     final YAMLImplementation<?> yi = YAMLImplementation.create( _cmdLineArgs.getYAMLLibrary() );
    //     super.setYAMLImplementation(  yi );
    //     if (_cmdLineArgs.verbose) System.out.println( HDR +" set YAML-Library to [" + _cmdLineArgs.getYAMLLibrary() + " === [" + super.getYAMLImplementation().getYAMLLibrary() + "]" );

    //     return super.getYAMLImplementation();
    // }

    //=================================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //=================================================================================

    /**
     *  This function is meant to be used by Cmd.main() and by BatchProcessor.java.  Read the code *FIRST*, to see if you can use this function too.
     *  @param _cmdLA will be Null for this class, as passed by {@link Cmd} or {@link org.ASUX.yaml.BatchCmdProcessor}.
     *  @param _ignore will be Null for this class, as passed by {@link Cmd} or {@link org.ASUX.yaml.BatchCmdProcessor}. Otherwise, this is the YAML inputData that is the input to pretty much all commands (a org.yaml.snakeyaml.nodes.Node object).
     *  @return either a String or org.yaml.snakeyaml.nodes.Node
     *  @throws FileNotFoundException if the filenames within this.cmdlineargsaws do NOT exist
     *  @throws IOException if the filenames within this.cmdlineargsaws give any sort of read/write troubles
     *  @throws Exception by ReplaceYamlCmd method and this nethod (in case of unknown command)
     */
    public Object processCommand ( org.ASUX.yaml.CmdLineArgsCommon _cmdLA, final Object _ignore )
                throws FileNotFoundException, IOException, Exception
    {
        assertTrue( _cmdLA instanceof CmdLineArgsAWS );
        this.cmdlineargsaws = (CmdLineArgsAWS) _cmdLA;
        final String HDR = CLASSNAME + ": processCommand("+ this.cmdlineargsaws.getCmdType() +",_inputData: ";// NOTE !!!!!! _cmdLA/CmdLineArgsCommon .. does NOT have 'cmdType' instance-variable

        final NodeTools nodetools = (NodeTools) super.getYAMLImplementation();
        assertTrue( nodetools != null );
        assertTrue( nodetools.getDumperOptions() != null );
        NodeTools.updateDumperOptions( nodetools.getDumperOptions(), _cmdLA.getQuoteType() ); // Important <<---------- <<---------- <<-----------

        final AWSSDK awssdk = AWSSDK.AWSCmdline( this.verbose, this.cmdlineargsaws.isOffline() );

        // aws.sdk ----list-regions
        // aws.sdk ----list-AZs         us-east-2
        // aws.sdk ----describe-AZs     us-east-2
        // in 'cmdLineArgsStrArr' below, we DROP the 'aws.sdk' leaving the rest of the parameters

        if (this.verbose) System.out.println( HDR +" cmdLineArgsStrArr has "+ this.cmdlineargsaws.getArgs().size() +" containing =["+ this.cmdlineargsaws.getArgs() +"]" );
        // the CmdLineArgsAWS class should take care of validating the # of cmd-line arguments and the type

        //-------------------------------------------
        switch( this.cmdlineargsaws.getCmdType() ) {
        case listRegions:       // "--list-regions"
            final ArrayList<String> regionsList = awssdk.getRegions( );
            final SequenceNode seqNode = NodeTools.ArrayList2Node( this.cmdlineargsaws.verbose, regionsList, nodetools.getDumperOptions() );
            return seqNode;
        //-------------------------------------------
        case getVPCID:           // simplifies use of 'describeVPCs' - just to get VPCID (of the 1st (perhaps only) VPC)
            final String vpcID = awssdk.getVPCID( this.cmdlineargsaws.getAWSRegion(), false );
            final ScalarNode scalar = new ScalarNode( Tag.STR, (vpcID==null?"null":vpcID), null, null, nodetools.getDumperOptions().getDefaultScalarStyle() ); // DumperOptions.ScalarStyle.PLAIN
            return scalar;
        //-------------------------------------------
        case describeVPCs:           // "--describe-vpcs"
            final ArrayList< LinkedHashMap<String,Object> > vpcList = awssdk.getVPCs( this.cmdlineargsaws.getAWSRegion(), false );
            final SequenceNode seqNode5 = NodeTools.ArrayList2Node( this.cmdlineargsaws.verbose, vpcList, nodetools.getDumperOptions() );
            return seqNode5;
        //-------------------------------------------
        case listAZs:           // "--list-AZs"
            final ArrayList<String> AZList = awssdk.getAZs( this.cmdlineargsaws.getAWSRegion() );
            final SequenceNode seqNode2 = NodeTools.ArrayList2Node( this.cmdlineargsaws.verbose, AZList, nodetools.getDumperOptions() );
            return seqNode2;
        //-------------------------------------------
        case describeAZs:       // "--describe-AZs"
            final ArrayList< LinkedHashMap<String,Object> > AZDetailsList = awssdk.describeAZs( this.cmdlineargsaws.getAWSRegion() );
            final SequenceNode seqNode3 = NodeTools.ArrayList2Node( this.cmdlineargsaws.verbose, AZDetailsList, nodetools.getDumperOptions() );
            return seqNode3;
        //-------------------------------------------
        case s364HexCanonicalId:       // "--s3-canonicalid-64char-hex"
            final String s364hexid = awssdk.getS3CanonicalUserId_as64digitHexadecimal( this.cmdlineargsaws.getAWSRegion() );
            final ScalarNode scalar64HexId = new ScalarNode( Tag.STR, (s364hexid==null?"null":s364hexid), null, null, nodetools.getDumperOptions().getDefaultScalarStyle() ); // DumperOptions.ScalarStyle.PLAIN
            return scalar64HexId;
        //-------------------------------------------
        case createKeyPair:     // "--create-keypair"
            final String AWSRegion = this.cmdlineargsaws.getAWSRegion();
            final String mySSHKeyName = this.cmdlineargsaws.getSubParameter2();
            final String keyMaterial = awssdk.createKeyPairEC2( AWSRegion, mySSHKeyName );

            //------------------
            final String homedir = System.getProperty("user.home");
            assertTrue( homedir != null );
            final File awsuserhome = new File( homedir +"/.aws" );
            awsuserhome.mkdirs();
            final String mySSHKeyFilePathStr = homedir +"/.aws/"+ mySSHKeyName;
            // final File mySSHKeyFile = new File( mySSHKeyFilePathStr );
            try {
                java.nio.file.Files.write(   java.nio.file.Paths.get( mySSHKeyFilePathStr ),   keyMaterial.getBytes()  );
                System.out.println( "File "+ mySSHKeyFilePathStr +" created." );
            // } catch(IOException ioe) {
            // } catch(IllegalArgumentException ioe) { // thrown by java.nio.file.Paths.get()
            // } catch(FileSystemNotFoundException ioe) { // thrown by java.nio.file.Paths.get()
            } catch(java.nio.file.InvalidPathException ipe) {
                ipe.printStackTrace( System.err );
                System.err.println( "\n\n"+ HDR +"!!SERIOUS INTERNAL ERROR!! Why would the Path '"+ mySSHKeyFilePathStr +"' be invalid?\n\n" );
                throw ipe;
            }
            final ScalarNode scalar2 = new ScalarNode( Tag.STR, keyMaterial, null, null, nodetools.getDumperOptions().getDefaultScalarStyle() ); // DumperOptions.ScalarStyle.PLAIN
            return scalar2;
        //-------------------------------------------
        case deleteKeyPair:     // "--delete-keypair"
            final String AWSRegion2 = this.cmdlineargsaws.getAWSRegion();
            final String mySSHKeyName2 = this.cmdlineargsaws.getSubParameter2();
            awssdk.deleteKeyPairEC2( AWSRegion2, mySSHKeyName2 );
            final Node n = NodeTools.getEmptyYAML( nodetools.getDumperOptions() );
            return n;
        //-------------------------------------------
        case listKeyPairs:     // "--delete-keypair"
            final String AWSRegion3 = this.cmdlineargsaws.getAWSRegion();
            String mySSHKeyName3 = this.cmdlineargsaws.getSubParameter2();
            if ( mySSHKeyName3 == null || "null".equals( mySSHKeyName3 ) || mySSHKeyName3.trim().length() <= 0 )
                mySSHKeyName3 = null;
            final List<com.amazonaws.services.ec2.model.KeyPairInfo> keys = awssdk.listKeyPairEC2( AWSRegion3, mySSHKeyName3 );
            final java.util.LinkedList<Node> seqs = new java.util.LinkedList<Node>();
            final SequenceNode seqNode4 = new SequenceNode( Tag.SEQ, false,     seqs,         null, null, nodetools.getDumperOptions().getDefaultFlowStyle() );
            for ( com.amazonaws.services.ec2.model.KeyPairInfo x: keys ) {
                if ( this.verbose) System.out.println( HDR +"DescribeKeyPairsResult KeyName=\n"+ x.getKeyName() );
                if ( this.verbose) System.out.println( HDR +"DescribeKeyPairsResult KeyFingerprint=\n"+ x.getKeyFingerprint() );
                // if ( this.verbose) System.out.println( HDR +"DescribeKeyPairsResult KeyMaterial=\n"+ x.getKeyMaterial() ); <<-- NOT AVAILABLE, AFTER KEY has been created
                // final ScalarNode k = new ScalarNode( Tag.STR, x.getKeyName(), null, null, nodetools.getDumperOptions().getDefaultScalarStyle() ); // DumperOptions.ScalarStyle.PLAIN
                // final ScalarNode v = new ScalarNode( Tag.STR, x.getKeyFingerprint(), null, null, nodetools.getDumperOptions().getDefaultScalarStyle() ); // DumperOptions.ScalarStyle.PLAIN
                // final java.util.List<NodeTuple> tuples = topNode.getValue();
                // tuples.add( new NodeTuple( k, v ) );
                final MappingNode mapNode = (MappingNode) NodeTools.getNewSingleMap( x.getKeyName(), x.getKeyFingerprint(), nodetools.getDumperOptions() );
                seqs.add ( mapNode );
            }
            return seqNode4;
        //-------------------------------------------
        case Undefined:         // 
        default:
            throw new Exception( "AWS.SDK INTERNAL-ERROR: Unknown command ["+ this.cmdlineargsaws.getArgs() +"]" );
        }

        //----------------------------------------
        // return null;

    }

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================
    /**
     * <p>This project (interface with AWS SDK) does Not need this functionality.</p>
     * <p>For other subclasses of {@link org.ASUX.yaml.CmdInvoker}, this method a simpler facade/interface to org.ASUX.yaml.InputsOutputs.getDataFromReference(), for use by org.ASUX.yaml.BatchCmdProcessor.</p>
     * @param _src a javalang.String value - either inline YAML/JSON, or a filename (must be prefixed with '@'), or a reference to a property within a Batch-file execution (must be prefixed with a '!')
     * @return an object (either any of Node, SequenceNode, MapNode, ScalarNode ..)
     * @throws FileNotFoundException if the filenames within this.cmdlineargsaws do NOT exist
     * @throws IOException if the filenames within this.cmdlineargsaws give any sort of read/write troubles
     * @throws Exception by ReplaceYamlCmd method and this nethod (in case of unknown command)
     */
    public Object getDataFromReference( final String _src )
                                throws FileNotFoundException, IOException, Exception
    {   //return InputsOutputs.getDataFromReference( _src, this.memoryAndContext, this.getYamlScanner(), nodetools.getDumperOptions(), this.verbose );
        final String HDR = CLASSNAME + ": getDataFromReference("+ _src +"): ";
        throw new Exception( "UNIMPLEMENTED METHOD: "+ HDR );
    }

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

    /**
     * <p>This project (interface with AWS SDK) does Not need this functionality.</p>
     * <p>For other subclasses of {@link org.ASUX.yaml.CmdInvoker}, this method a simpler facade/interface to org.ASUX.yaml.InputsOutputs.getDataFromReference(), for use by org.ASUX.yaml.BatchCmdProcessor</p>
     * @param _dest a javalang.String value - either a filename (must be prefixed with '@'), or a reference to a (new) property-variable within a Batch-file execution (must be prefixed with a '!')
     * @param _input the object to be saved using the reference provided in _dest paramater
     * @throws FileNotFoundException if the filenames within this.cmdlineargsaws do NOT exist
     * @throws IOException if the filenames within this.cmdlineargsaws give any sort of read/write troubles
     * @throws Exception by ReplaceYamlCmd method and this nethod (in case of unknown command)
     */
    public void saveDataIntoReference( final String _dest, final Object _input )
                            throws FileNotFoundException, IOException, Exception
    {
        // InputsOutputs.saveDataIntoReference( _dest, _input, this.memoryAndContext, this.getYamlWriter(), nodetools.getDumperOptions(), this.verbose );
        final String HDR = CLASSNAME + ": saveDataIntoReference("+ _dest +","+ _input.getClass().getName() +"): ";
        throw new Exception( "UNIMPLEMENTED METHOD: "+ HDR );
    }

    //==============================================================================
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    //==============================================================================

}
