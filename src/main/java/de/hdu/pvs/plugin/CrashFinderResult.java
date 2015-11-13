package de.hdu.pvs.plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.hdu.pvs.utils.DocumentReader;

public class CrashFinderResult {
	public ArrayList<String> CrashFinderDifferencer(ArrayList<String> passReader,
			ArrayList<String> failReader) throws IOException {
		ArrayList<String> diffCoverage = new ArrayList<String>();
		boolean check = true;
		for (String fr : failReader) {
			String frRegex = fr.split("#")[0]
					+ fr.split("#")[1].split("\\(")[0];
			for (String pr : passReader) {
				check = false;
				String prRegex = pr.split("#")[0]
						+ pr.split("#")[1].split("\\(")[0];
				if (prRegex.equals(frRegex)) {
					check = true;
					break;
				}
			}
			if (!check) {
				check = true;
				diffCoverage.add(fr);
			}
		}
		return diffCoverage;
	}

	public ArrayList<String> CrashFinderMapper(File shrikePassing,
			File shrikeFailing, ArrayList<String> diffCoverage) throws IOException {
		ArrayList<String> diffCoverageLoc = new ArrayList<String>();
		ArrayList<String> failingShrikeReader = DocumentReader
				.slurpFiles(shrikeFailing);
		ArrayList<String> passingShrikeReader = DocumentReader
				.slurpFiles(shrikePassing);
		for (String item : diffCoverage) {
			String classInst = item.split("\\(")[0];
			String instIndex = item.substring(item.lastIndexOf("#") + 1).trim();
			for (String failSh : failingShrikeReader) {
				String shIndex = failSh.split("instruction index: ")[1].split(
						" ", 2)[0];
				String shClass = failSh.split(" @ ")[1].split("\\(")[0];
				String lineNum = failSh.split("line num: ")[1].split(" @ ")[0];
				if (classInst.equals(shClass) && instIndex.equals(shIndex)) {
					System.out.println(classInst + ":" + lineNum);
					diffCoverageLoc.add(classInst + ":" + lineNum);
					continue;
				}
			}
		}
		return diffCoverageLoc;
	}
}
