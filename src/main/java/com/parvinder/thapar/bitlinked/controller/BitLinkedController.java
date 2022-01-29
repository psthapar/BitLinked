package com.parvinder.thapar.bitlinked.controller;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.parvinder.thapar.bitlinked.beans.BitLinkedProfile;
import com.parvinder.thapar.bitlinked.blockchain.BlockchainAsset;
import com.parvinder.thapar.bitlinked.exception.BitlinkedException;
import com.parvinder.thapar.bitlinked.exception.BitlinkedExceptionCode;
import com.parvinder.thapar.bitlinked.jpa.BitLinkedProfileRepository;
import com.parvinder.thapar.bitlinked.rest.RestConstants;
import com.parvinder.thapar.bitlinked.rest.SimpleCryptoProfile;
import com.parvinder.thapar.bitlinked.rest.SimpleCryptoProfileDetail;
import com.parvinder.thapar.bitlinked.rest.SimpleResponse;
import com.parvinder.thapar.bitlinked.rest.SimpleSearchResults;
import com.parvinder.thapar.bitlinked.search.BitlinkedSearchEngine;
import com.parvinder.thapar.bitlinked.search.SearchEngine;
import com.parvinder.thapar.bitlinked.util.GlobalConstants;
import com.parvinder.thapar.bitlinked.util.GlobalUtil;

@Controller
public class BitLinkedController {

	@Autowired
	BitLinkedProfileRepository profileRepository;
	
	private static final SearchEngine simpleSearchEngine = new BitlinkedSearchEngine();
	private boolean isApplicationInitialized = false;
	
	/**
	 * Run Only once during Initialization
	 */
	@SuppressWarnings("rawtypes")
	protected void initializeApplication(JpaRepository activeProfileRepository) {
		System.out.println("\n\n\n=========>>>Running Initialization for the app <<<==============\n\n\n");
		
		try {
			if(!isApplicationInitialized) {
				simpleSearchEngine.setContext(activeProfileRepository);
				simpleSearchEngine.fullIndexSearchEngine();
				isApplicationInitialized = true;
			}
		} catch (Exception ex) {
			System.out.println("\n====>Exception occured while indexing new Cryptoprofile : " + ex.getLocalizedMessage());
			ex.printStackTrace();
		}
	}
	
/*
	@SuppressWarnings("rawtypes")
	@ResponseBody
	@GetMapping(value = "/")
	public ResponseEntity index() {
		initializeApplication(profileRepository);
		return ResponseEntity.ok(profileRepository.findAll());
	}
*/
	
	@GetMapping(value = "/")
	public ModelAndView index() {
		initializeApplication(profileRepository);
		ModelAndView model = new ModelAndView("/home");
		return model;
	}
	
	@GetMapping(value = "/home")
	public ModelAndView  home() {
		ModelAndView model = new ModelAndView("/home");
		return model;
	}	
	
	@GetMapping(value = "/testapi")
	public ModelAndView  testapi() {
		ModelAndView model = new ModelAndView("/testapi");
		model.addObject("message", "Welcome to BitLinked MicroServices");
		return model;
	}

	@GetMapping(value = "/recruiter")
	public ModelAndView  recruiter() {
		ModelAndView model = new ModelAndView("/recruiter");
		return model;
	}
	
	@GetMapping(value = "/seeker")
	public ModelAndView  seeker() {
		ModelAndView model = new ModelAndView("/seeker");
		return model;
	}
	
	
	
	@SuppressWarnings("rawtypes")
	@ResponseBody
	@GetMapping(value = "/listCryptoProfiles")
	@PostMapping(value = "/listCryptoProfiles")
	public ResponseEntity listCryptoProfiles() {
		try {
			List<SimpleCryptoProfile> allCryptoUserProfiles = simpleSearchEngine.findAllItemHeaders();
			
			System.out.println("\n===>Number of Crypto User Profiles: " + allCryptoUserProfiles.size());
			
			return ResponseEntity.ok(allCryptoUserProfiles);
		} catch (Exception ex) {
			return ResponseEntity.ok(new SimpleResponse(RestConstants.REQ_TYPE_SHOW_ALL_PROFILES, RestConstants.OPERATION_FAIL, new BitlinkedException(BitlinkedExceptionCode.SHOW_ALL_PROFILES, ex.getMessage())));
		}
	}

	/*
	 * @RequestMapping(value = "/testapi", method = RequestMethod.GET) public String
	 * testapi(Locale locale, Model model) { return "testapi"; }
	 */	
	@SuppressWarnings("rawtypes")
	@ResponseBody
	@PostMapping(value = "/createCryptoProfile")
	public ResponseEntity createCryptoProfile(	@RequestParam(value="email") String email,
												@RequestParam(value="name") String name,
												@RequestParam(value="summaryprofile") String summaryprofile
											) {
		System.out.println("\n\n====>Request Parameters: <=======  "  + email + ", " + name + ", " + summaryprofile);

		System.out.println("\n\n====>Checking Email Formatting <=======  "  + email);
		// 0. Validate UserId Format
		if (false == ControllerUtil.isUserIdValidFormat(email)) {
			return ResponseEntity.ok(new BitlinkedException(BitlinkedExceptionCode.INCORRECT_USER_ID, "Email " + email + " incorrect. Please try again with valid user email id"));
		}

		System.out.println("\n\n====>Checking whether email already exists <=======");
		// 1. Check whether user exists
		if (profileRepository.findById(email).isPresent()) {
			return ResponseEntity.ok(new BitlinkedException(BitlinkedExceptionCode.PROFILE_ALREADY_EXISTS, "Email " + email + " already exists. Please try with a different Email"));
		}

		System.out.println("\n\n====>Creating Cryprto Profile <=======");
		// 2. Create Big Chain CryptoProfile 
		BlockchainAsset blockchainAsset = null;
		try {
			blockchainAsset = ControllerUtil.createBigChainCryptoProfile(email);	
		}catch (Exception ex) {
			ex.printStackTrace();
			return ResponseEntity.ok(new Exception (ex.getMessage()));
		}

		System.out.println("\n\n====>Creating Crypto Profile done <======= " + blockchainAsset);
		if(null == blockchainAsset) {
			return ResponseEntity.ok("Creating a Crypto Profile Failed. Contact your System Administrator");
		}

		System.out.println("\n\n====>Saving it to DB <======= " + blockchainAsset);
		BitLinkedProfile newBitLinkedProfile = new BitLinkedProfile(email, name, summaryprofile);
		newBitLinkedProfile.setEncodedKeyPair(blockchainAsset.getBlockchainEncodedKeys());
		newBitLinkedProfile.setProfileAssetId(blockchainAsset.getBlockchainAssetId());

		// 3. If successful, create metadata record in Application DB
		profileRepository.save(newBitLinkedProfile);
		
		// 4. Update Search Index
		try {
			simpleSearchEngine.incrementalIndexSearchEngine(newBitLinkedProfile);
		}catch (Exception ex) {
			System.out.println("\n====>Exception occured while indexing new Cryptoprofile : " + ex.getLocalizedMessage());
			ex.printStackTrace();
		}

		return ResponseEntity.ok(new SimpleResponse(RestConstants.REQ_TYPE_CREATE_CRYPTO_PROFILE, RestConstants.OPERATION_SUCCESS));
	}

	@SuppressWarnings("rawtypes")
	@ResponseBody
	@PostMapping(value = "/addEducation")
	public ResponseEntity addEducation(	@RequestParam(value="email") String email, 
										@RequestParam(value="institutionName") String institutionName, 
										@RequestParam(required=false, value="institutionLocation") String institutionLocation,
										@RequestParam(value="degree") String degree,
										@RequestParam(value="fieldOfStudy") String fieldOfStudy,
										@RequestParam(value="startDate") String startDate,
										@RequestParam(required=false, value="endDate") String endDate,
										@RequestParam(required=false, value="degreeReceivedDate") String degreeReceivedDate,
										@RequestParam(required=false, value="gradeAverage") String gradeAverage,
										@RequestParam(value="status") String status, // Completed or not
										@RequestParam(required=false, value="isVerified") Boolean isVerified) {
		
		System.out.println("\n\n====>Inside addEducation <======== \n\n" );
		
		Map<String, String> educationBlock = new LinkedHashMap<String, String>();
		educationBlock.put(GlobalConstants.BLOCK_TYPE, GlobalConstants.BLOCK_TYPE_EDUCATION);
		educationBlock.put(GlobalConstants.EDU_INSTITUTION_NAME, institutionName.trim());
		educationBlock.put(GlobalConstants.EDU_INSTITUTION_LOC, null==institutionLocation?"":institutionLocation.trim());
		educationBlock.put(GlobalConstants.EDU_DEGREE, degree.trim());
		educationBlock.put(GlobalConstants.EDU_FIELD_OF_STUDY, fieldOfStudy.trim());
		educationBlock.put(GlobalConstants.EDU_YEAR_STARTED, startDate.trim());
		educationBlock.put(GlobalConstants.EDU_YEAR_COMPLETED, null==endDate?"": endDate.trim());
		educationBlock.put(GlobalConstants.EDU_DEGREE_RECEIVED_DATE, null==degreeReceivedDate?"":degreeReceivedDate.trim());
		educationBlock.put(GlobalConstants.EDU_AVG_GRADE, null==gradeAverage?"":gradeAverage.trim());
		educationBlock.put(GlobalConstants.EDU_STATUS, status.trim());
		educationBlock.put(GlobalConstants.IS_BLOCK_VERIFIED, null==isVerified?Boolean.FALSE.toString(): isVerified.toString());
		Date todayWithTimeStamp = GlobalUtil.getTodayWithTimeStamp();
		educationBlock.put(GlobalConstants.BLOCK_CREATE_DATE, todayWithTimeStamp.toString());
		
		if(null == email || email.trim().length() == 0 || false == ControllerUtil.isUserIdValidFormat(email)) {
			return ResponseEntity.ok(new SimpleResponse(RestConstants.REQ_TYPE_ADD_EDUCATION, RestConstants.OPERATION_FAIL, new BitlinkedException(BitlinkedExceptionCode.INCORRECT_USER_ID, "Email " + email + " incorrect. Please try again with valid user email id")));
		}
		
		if (!profileRepository.findById(email).isPresent()) {
			return ResponseEntity.ok(new SimpleResponse(RestConstants.REQ_TYPE_ADD_EDUCATION, RestConstants.OPERATION_FAIL, new BitlinkedException(BitlinkedExceptionCode.PROFILE_DOES_NOT_EXIST, "A Profile with email " + email + " does not exist. Please try creating a Profile before adding Education.")));
		}
		
		BitLinkedProfile bitLinkedProfile = profileRepository.findById(email).get();
		// 1. Add the Block to existing Profile
		try {
			ControllerUtil.addNewBlock(bitLinkedProfile, educationBlock);
		}catch (Exception ex) {
			return ResponseEntity.ok(new SimpleResponse(RestConstants.REQ_TYPE_ADD_EDUCATION, RestConstants.OPERATION_FAIL, new BitlinkedException(BitlinkedExceptionCode.ADDING_BLOCK_FAILED, ex.getMessage())));
		}
		
		// 2. Update the Profile Update Date to existing Profile
		bitLinkedProfile.setUpdateDate(todayWithTimeStamp.toString());
		profileRepository.save(bitLinkedProfile);
		
		// 4. Update Search Index
		try {
			simpleSearchEngine.updateIndexSearchEngine(bitLinkedProfile);
		}catch (Exception ex) {
			System.out.println("\n====>Exception occured while indexing new Cryptoprofile : " + ex.getLocalizedMessage());
			ex.printStackTrace();
		}
		
		return ResponseEntity.ok(new SimpleResponse(RestConstants.REQ_TYPE_ADD_EDUCATION, RestConstants.OPERATION_SUCCESS));
	}

	@SuppressWarnings("rawtypes")
	@ResponseBody
	@PostMapping(value = "/addEmployment")
	public ResponseEntity addEmployment(@RequestParam(value="email") String email,
										@RequestParam("jobTitle") String jobTitle,
										@RequestParam("company") String company,
										@RequestParam("jobLocation") String jobLocation,
										@RequestParam("jobStartDate") String jobStartDate,
										@RequestParam(required=false, value="jobEndDate") String jobEndDate,
										@RequestParam(required=false, value="hiringManager") String hiringManager,
										@RequestParam(required=false, value="jobProfileSummary") String jobProfileSummary,
										@RequestParam(required=false, value="isVerified") Boolean isVerified) {
		
		System.out.println("\n\n====>Inside addEmployment <======== \n\n" );
		
		Map<String, String> employmentBlock = new LinkedHashMap<String, String>();
		employmentBlock.put(GlobalConstants.BLOCK_TYPE, GlobalConstants.BLOCK_TYPE_EMPLOYMENT);
		employmentBlock.put(GlobalConstants.EMP_JOB_TITLE, jobTitle.trim());
		employmentBlock.put(GlobalConstants.EMP_COMPANY, company.trim());
		employmentBlock.put(GlobalConstants.EMP_LOCATION, jobLocation.trim());
		employmentBlock.put(GlobalConstants.EMP_START_DATE, jobStartDate.trim());
		employmentBlock.put(GlobalConstants.EMP_END_DATE, null==jobEndDate?"": jobEndDate.trim());
		employmentBlock.put(GlobalConstants.EMP_HIRING_MANAGER, null==hiringManager?"": hiringManager.trim());
		employmentBlock.put(GlobalConstants.EMP_JOB_SUMMARY, null==jobProfileSummary?"": jobProfileSummary.trim());
		employmentBlock.put(GlobalConstants.IS_BLOCK_VERIFIED, null==isVerified?Boolean.FALSE.toString(): isVerified.toString());
		Date todayWithTimeStamp = GlobalUtil.getTodayWithTimeStamp();
		employmentBlock.put(GlobalConstants.BLOCK_CREATE_DATE, todayWithTimeStamp.toString());
		
		if(null == email || email.trim().length() == 0 || false == ControllerUtil.isUserIdValidFormat(email)) {
			return ResponseEntity.ok(new SimpleResponse(RestConstants.REQ_TYPE_ADD_EMPLOYMENT, RestConstants.OPERATION_FAIL, new BitlinkedException(BitlinkedExceptionCode.INCORRECT_USER_ID, "Email " + email + " incorrect. Please try again with valid user email id"))); 
		}
		
		if (!profileRepository.findById(email).isPresent()) {
			return ResponseEntity.ok(new SimpleResponse(RestConstants.REQ_TYPE_ADD_EMPLOYMENT, RestConstants.OPERATION_FAIL, new BitlinkedException(BitlinkedExceptionCode.PROFILE_DOES_NOT_EXIST, "A Profile with email " + email + " does not exist. Please try creating a Profile before adding Education.")));
		}
		
		BitLinkedProfile bitLinkedProfile = profileRepository.findById(email).get();
		// 1. Add the Block to existing Profile
		try {
			ControllerUtil.addNewBlock(bitLinkedProfile, employmentBlock);
		}catch (Exception ex) {
			return ResponseEntity.ok(new SimpleResponse(RestConstants.REQ_TYPE_ADD_EMPLOYMENT, RestConstants.OPERATION_FAIL, new BitlinkedException(BitlinkedExceptionCode.ADDING_BLOCK_FAILED, ex.getMessage())));
		}
		
		// 2. Update the Profile Update Date to existing Profile
		bitLinkedProfile.setUpdateDate(todayWithTimeStamp.toString());
		profileRepository.save(bitLinkedProfile);
		
		// 4. Update Search Index
		try {
			simpleSearchEngine.updateIndexSearchEngine(bitLinkedProfile);
		}catch (Exception ex) {
			System.out.println("\n====>Exception occured while indexing new Cryptoprofile : " + ex.getLocalizedMessage());
			ex.printStackTrace();
		}
		
		return ResponseEntity.ok(new SimpleResponse(RestConstants.REQ_TYPE_ADD_EMPLOYMENT, RestConstants.OPERATION_SUCCESS));
	}

	@SuppressWarnings("rawtypes")
	@ResponseBody
	@GetMapping(value = "/showCryptoProfile")
	@PostMapping(value = "/showCryptoProfile")
	public ResponseEntity showCryptoProfile (@RequestParam(value="email") String email) {
		
		if(null == email || email.trim().length() == 0 || false == ControllerUtil.isUserIdValidFormat(email)) {
			return ResponseEntity.ok(new SimpleResponse(RestConstants.REQ_SHOW_CRYPTO_BLOCKS, RestConstants.OPERATION_FAIL, new BitlinkedException(BitlinkedExceptionCode.INCORRECT_USER_ID, "Email " + email + " incorrect. Please try again with valid user email id")));
		}
		
		if (!profileRepository.findById(email).isPresent()) {
			return ResponseEntity.ok(new SimpleResponse(RestConstants.REQ_SHOW_CRYPTO_BLOCKS, RestConstants.OPERATION_FAIL, new BitlinkedException(BitlinkedExceptionCode.PROFILE_DOES_NOT_EXIST, "A Profile with email " + email + " does not exist. Please try creating a Profile before adding Education.")));
		}
		
		BitLinkedProfile bitLinkedProfile = profileRepository.findById(email).get();
		SimpleCryptoProfile simpleCryptoProfile = new SimpleCryptoProfileDetail(bitLinkedProfile.getEmail(), bitLinkedProfile.getName(), bitLinkedProfile.getSummaryprofile(), bitLinkedProfile.getCreateDate(), bitLinkedProfile.getUpdateDate());
		try {
			simpleCryptoProfile.setCryptoBlocks(ControllerUtil.retrieveCryptoBlocks(bitLinkedProfile));
		}catch (Exception ex) {
			return ResponseEntity.ok(new SimpleResponse(RestConstants.REQ_SHOW_CRYPTO_BLOCKS, RestConstants.OPERATION_FAIL, new BitlinkedException(BitlinkedExceptionCode.RETRIEVING_BLOCKS_FAILED, ex.getMessage())));
		}
		return ResponseEntity.ok(simpleCryptoProfile);
	}
	
	@SuppressWarnings("rawtypes")
	@ResponseBody
	@GetMapping(value = "/searchCryptoProfiles")
	@PostMapping(value = "/searchCryptoProfiles")
	public ResponseEntity searchCryptoProfiles(@RequestParam("q") String queryString) {
		try {
			System.out.println("\n===>Search Query: " + queryString);
			
			List<SimpleCryptoProfile> searchResults = simpleSearchEngine.findItems(queryString);
			
			System.out.println("===>Number of Crypto User Profiles: " + searchResults.size());
			
			SimpleSearchResults simpleSearchResults = new SimpleSearchResults(queryString, searchResults.size(), searchResults);
			
			return ResponseEntity.ok(simpleSearchResults);
		} catch (Exception ex) {
			return ResponseEntity.ok(new SimpleResponse(RestConstants.REQ_TYPE_SEARCH_PROFILES, RestConstants.OPERATION_FAIL, new BitlinkedException(BitlinkedExceptionCode.SEARCH_PROFILES, ex.getMessage())));
		}
	}
	

}
