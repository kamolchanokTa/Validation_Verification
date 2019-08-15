package com.wuerth.phoenix.cis.university.example2.test.milk;

import com.wuerth.phoenix.cis.university.example2.test.milk.EngineTestData;
import com.wuerth.phoenix.cis.university.example2.test.sample.SampleEngineTestData;
import com.wuerth.phoenix.cis.university.example2.util.FileGenerator;
import com.wuerth.phoenix.cis.university.example2.util.Scenario;

// cocpy code from Doc2 of ex2
public class EngineTest { //extends AbstractCISTest {
	public static int i = 0;
	public EngineTest(String testCaseName) {
		//super(testCaseName);
	}
	
	public static void main(String [ ] args) {
		testSample();
	}

	public static void testSample() {
		// new SampleEngineTestData() {
		new SampleEngineTestData() {
			
			@Override
			public boolean check(Scenario scenario) {
				//checkImport(scenario);
				try {
					System.out.println(i++);
//					FileGenerator.generate(scenario);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return true;
			}
		}.start();
	}
	
	
	private void checkImport(Scenario scenario) {
		
		System.out.println("START...");
		long time = System.currentTimeMillis();
		
		// Data generation
//		new ATransactionOperation<Boolean>(this, false) {
//
//			@Override
//			protected Boolean operation() throws PUserException {
//				DataGenerator.generate(
//					scenario.getCompanyList(), 
//					scenario.getSettings().isLongVersion(), 
//					getDbController()
//					);
//				return true;
//			}
//		}.execute();
//		
//		String errorMessage = new ATransactionOperation<String>(this, false) {
//
//			@Override
//			protected String operation() throws PUserException {
//				CisController controller = getDbController();
//				IFRS16ImportCompanyDataEngine engine = new IFRS16ImportCompanyDataEngine(controller);
//				ImportChecker checker = new ImportChecker();
//
//				// Check before import
//				String errorMessage = checker.checkBeforeImport(scenario, engine, controller);
//				if(errorMessage != null) {
//					return errorMessage;
//				}
//				
//				// File generation
//				try {
//					FileGenerator.generate(scenario);
//				}
//				catch(Exception exception) {
//					exception.printStackTrace();
//					return "An exception occurred during the import file generation!";
//				}
//
//				// Use panels
//				try {
//					Importer.usePanels(scenario, engine, controller);
//				}
//				catch(Exception exception) {
//					exception.printStackTrace();
//					return "An exception occurred during the engine use!";
//				}
//
//				// Check data parsed
//				errorMessage = checker.checkDataParsed(scenario, engine, controller);
//				if(errorMessage != null) {
//					return errorMessage;
//				}
//
//				// Check data merged
//				errorMessage = checker.checkDataMerged(scenario, engine, controller);
//				if(errorMessage != null) {
//					return errorMessage;
//				}
//
//				// Import
//				try {
//					Importer.doImport(engine);
//				}
//				catch(Exception exception) {
//					exception.printStackTrace();
//					return "An exception occurred during the import!";
//				}
//
//				// Check after import
//				errorMessage = checker.checkAfterImport(scenario, engine, controller);
//				if(errorMessage != null) {
//					return errorMessage;
//				}
//
//				return null;
//			}
//		}.execute();
//		AbstractULCTest.assertTrue(errorMessage, errorMessage == null);
//		
//		System.out.println("...END (" + (System.currentTimeMillis() - time) + " ms)");
	}


}
