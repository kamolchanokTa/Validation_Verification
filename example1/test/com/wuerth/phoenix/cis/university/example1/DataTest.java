package com.wuerth.phoenix.cis.university.example1;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.wuerth.phoenix.cis.university.example1.adapters.IData;
import com.wuerth.phoenix.cis.university.example1.concretes.ConcreteCompany;
import com.wuerth.phoenix.cis.university.example1.input.CSVReader;

@RunWith(Parameterized.class)
public class DataTest {
	static Example1Checker example1Checker;

	private static class IDataAndExpectedOutput {
		private IData iData;
		private boolean expectedOutput;

		public IDataAndExpectedOutput(IData iData, boolean expectedOutput) {
			this.iData = iData;
			this.expectedOutput = expectedOutput;
		}

		public IData getIData() {
			return iData;
		}

		public boolean getExpectedOutput() {
			return expectedOutput;
		}
	}


	@Parameterized.Parameter
	public IDataAndExpectedOutput IDataAndExpectedOutput;

	@Before
	public void initialize() {
		example1Checker = new Example1Checker();	
	}

	@Parameterized.Parameters
	public static Collection<IDataAndExpectedOutput> getDatas() {
		CSVReader csvReader = new CSVReader();
		ArrayList<IData> listValidIData = csvReader.readCSV("valid_combinations.csv", 14);

		return getIDataAndExpectedOutput(listValidIData, true).collect(Collectors.toList());
	}

	private static Stream<IDataAndExpectedOutput> getIDataAndExpectedOutput (ArrayList<IData> listIData, boolean expectedOutput) {
		return listIData.stream().map(iData -> new IDataAndExpectedOutput(iData, expectedOutput));
	}

	/**
	 * Test combinations for Data
	 */
	@Test
	public void ValidCombinationData() {
		IData testedIData = IDataAndExpectedOutput.getIData();
		boolean expectedValue = IDataAndExpectedOutput.getExpectedOutput();
		assertEquals(example1Checker.isValid(new ConcreteCompany(), testedIData.getProfitCenter(), testedIData.getCRComponent(),
				testedIData.isExtern(), testedIData.getScenario(), testedIData.getAccount(), testedIData.getParternCode(), testedIData.getCurrencyCode()), expectedValue);
	}
}