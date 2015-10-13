__author__ = 'Mohammadreza Ghanavati'
__email__ = "mohammadreza.ghanavati@informatik.uni-heidelberg.de"


from unidiff import parse_unidiff, LINE_TYPE_ADD, LINE_TYPE_DELETE


def findSeed(failingSeed, diffFilePath):
    """
    Finds seed statement for the passing version from diff file and the seed statement of the failing version
    using unidiff module
    return: seed statement for the passing version as a string 'srcFile:linenumber'
    """
    srcFile = failingSeed.split(':')[0].replace('.', '/')
    #print srcFile
    failingSeedLineNum = int(failingSeed.split(':')[1])
    #print failingSeedLineNum

    parser = parse_unidiff(open(diffFilePath))
    passingSeedLineNum = failingSeedLineNum
    modifiedLines = []
    for parsed in parser:
        if srcFile in str(parsed):
            #print str(parsed)
            if '.java' in str(parsed) and srcFile in str(parsed):
                for hunk in parsed:
                    #print hunk.target_start
                    if hunk.target_start < failingSeedLineNum:
                        #print hunk
                        """ hunk contains one block of modified code. List hunk.target_lines contains
                            the lines in the target (new) version, and list hunk.target_types gives
                            change type of each line (e.g. '+' == added). There are also hunk.target_length
                            and corresponding fields hunk.source_*
                        """
                        modifiedLines += hunk.target_types + hunk.source_types
                        addedLines = modifiedLines.count('+')
                        deletedLines = modifiedLines.count('-')
                        passingSeedLineNum = failingSeedLineNum - addedLines + deletedLines
    passingSeed = ('%s:%s' % (srcFile, passingSeedLineNum)).replace('/','.')
    return passingSeed


def main(diffFilePath,failingSeed):
    #diffFilePath = '/home/felix/.jenkins/jobs/fail/workspace/diff.diff'
    #failingSeed = 'org.apache.hadoop.hdfs.server.namenode.FSNamesystem:1019'
    #diffFilePath = diffFilePat
    #failingSeed = sys.argv[2]
    passingSeed = findSeed(failingSeed, diffFilePath)
    print failingSeed
    print passingSeed

if __name__ == "__main__":
	
    diffFilePath = sys.argv[1]
    failingSeed = sys.argv[2]
    main(diffFilePath,failingSeed)
