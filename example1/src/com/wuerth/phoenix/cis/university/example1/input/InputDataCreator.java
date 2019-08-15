package com.wuerth.phoenix.cis.university.example1.input;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.wuerth.phoenix.cis.university.example1.concretes.ConcreteAccount;
import com.wuerth.phoenix.cis.university.example1.types.AccountClass;
import com.wuerth.phoenix.cis.university.example1.types.AccountType;

public class InputDataCreator {
	private static boolean currentBoolValue = true;
	private static CSVReader csvReader = new CSVReader();
	
	private static final String INVALID_COMBINATIONS_RESULT = "invalid_combinations.csv";
	private static final String VALID_COMBINATIONS_RESULT = "valid_combinations.csv";
	
	private static final String VALID_COMBINATIONS_GIVEN = "Account.csv";


	public static void main(String [ ] args) {
		generateAccountData();
	}
	
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    Set<Object> seen = ConcurrentHashMap.newKeySet();
	    return t -> seen.add(keyExtractor.apply(t));
	}

	public static void generateAccountData() {
		// Read all the valid accounts
		ArrayList<ConcreteAccount> valid_accounts = csvReader.readAccountCSV(VALID_COMBINATIONS_GIVEN, 4, 1, 2, 3);

		BufferedWriter bwValid = null;
		FileWriter fwValid = null;
		BufferedWriter bwInvalid = null;
		FileWriter fwInvalid = null;

		try {

			File fileValid = new File(VALID_COMBINATIONS_RESULT);
			File fileInvalid = new File(INVALID_COMBINATIONS_RESULT);

			if (!fileValid.exists()) {
				fileValid.createNewFile();
			}

			if (!fileInvalid.exists()) {
				fileInvalid.createNewFile();
			}

			fwValid = new FileWriter(fileValid.getAbsoluteFile(), false);
			bwValid = new BufferedWriter(fwValid);

			fwInvalid = new FileWriter(fileInvalid.getAbsoluteFile(), false);
			bwInvalid = new BufferedWriter(fwInvalid);

			// Loop over all possible combinations of accountType x accountClass
			for (AccountClass accountClass : AccountClass.values()) {
				for (AccountType accountType : AccountType.values()) {

					boolean accountIsValid = valid_accounts.stream().
							anyMatch(account -> account.accountClass == accountClass && account.accountType == accountType && account.partnerAllowed == currentBoolValue);
					boolean isAccountValid = valid_accounts.stream().
							anyMatch(account -> account.accountClass == accountClass && account.accountType == accountType );

					String accountTypeAdjusted = accountType.toString();
					if(accountType == AccountType.Empty) {
						accountTypeAdjusted = "";
					}

					String accountLine = "";
					if(accountIsValid) {
						accountLine = csvReader.combineAccountCSVLine(accountTypeAdjusted, accountClass.toString(), String.valueOf(currentBoolValue)); 
						bwValid.write(accountLine);
					}
					else {
						accountLine = csvReader.combineAccountCSVLine(accountTypeAdjusted, accountClass.toString(), String.valueOf(currentBoolValue));
						bwInvalid.write(accountLine);
					}
					// To find the existing accounts from Account.csv which doesn't generate automatically.
					if(isAccountValid) {
						List<ConcreteAccount> validAccounts = valid_accounts.stream()
								.filter(p -> p.accountClass == accountClass && p.accountType == accountType && p.partnerAllowed != currentBoolValue)
								.filter(distinctByKey(ConcreteAccount::getAccountClass)).collect(Collectors.toList());
						for(ConcreteAccount uniqueValue: validAccounts) {
							accountLine = csvReader.combineAccountCSVLine(accountTypeAdjusted, accountClass.toString(), String.valueOf(uniqueValue.isPartnerAllowed())); 
							bwValid.write(accountLine);
						}
					}
					currentBoolValue = (currentBoolValue==true ? false : true);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();

		} finally {

			try {

				if (bwValid != null)
					bwValid.close();

				if (fwValid != null)
					fwValid.close();

				if (bwInvalid != null)
					bwInvalid.close();

				if (fwInvalid != null)
					fwInvalid.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}
	}
}
