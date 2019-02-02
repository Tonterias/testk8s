package com.test.k8s.web.rest;

import com.test.k8s.Testk8SApp;

import com.test.k8s.domain.Profile;
import com.test.k8s.repository.ProfileRepository;
import com.test.k8s.service.ProfileService;
import com.test.k8s.service.dto.ProfileDTO;
import com.test.k8s.service.mapper.ProfileMapper;
import com.test.k8s.web.rest.errors.ExceptionTranslator;
import com.test.k8s.service.dto.ProfileCriteria;
import com.test.k8s.service.ProfileQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static com.test.k8s.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.test.k8s.domain.enumeration.Gender;
/**
 * Test class for the ProfileResource REST controller.
 *
 * @see ProfileResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Testk8SApp.class)
public class ProfileResourceIntTest {

    private static final Instant DEFAULT_CREATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_BIO = "AAAAAAAAAA";
    private static final String UPDATED_BIO = "BBBBBBBBBB";

    private static final String DEFAULT_FACEBOOK = "AAAAAAAAAA";
    private static final String UPDATED_FACEBOOK = "BBBBBBBBBB";

    private static final String DEFAULT_TWITTER = "AAAAAAAAAA";
    private static final String UPDATED_TWITTER = "BBBBBBBBBB";

    private static final String DEFAULT_LINKEDIN = "AAAAAAAAAA";
    private static final String UPDATED_LINKEDIN = "BBBBBBBBBB";

    private static final String DEFAULT_INSTAGRAM = "AAAAAAAAAA";
    private static final String UPDATED_INSTAGRAM = "BBBBBBBBBB";

    private static final String DEFAULT_OPTIONALSN = "AAAAAAAAAA";
    private static final String UPDATED_OPTIONALSN = "BBBBBBBBBB";

    private static final Instant DEFAULT_BIRTHDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BIRTHDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_SIBBLINGS = -1;
    private static final Integer UPDATED_SIBBLINGS = 0;

    private static final Boolean DEFAULT_PET = false;
    private static final Boolean UPDATED_PET = true;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileMapper profileMapper;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileQueryService profileQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restProfileMockMvc;

    private Profile profile;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProfileResource profileResource = new ProfileResource(profileService, profileQueryService);
        this.restProfileMockMvc = MockMvcBuilders.standaloneSetup(profileResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profile createEntity(EntityManager em) {
        Profile profile = new Profile()
            .creationDate(DEFAULT_CREATION_DATE)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .gender(DEFAULT_GENDER)
            .phone(DEFAULT_PHONE)
            .bio(DEFAULT_BIO)
            .facebook(DEFAULT_FACEBOOK)
            .twitter(DEFAULT_TWITTER)
            .linkedin(DEFAULT_LINKEDIN)
            .instagram(DEFAULT_INSTAGRAM)
            .optionalsn(DEFAULT_OPTIONALSN)
            .birthdate(DEFAULT_BIRTHDATE)
            .sibblings(DEFAULT_SIBBLINGS)
            .pet(DEFAULT_PET);
        return profile;
    }

    @Before
    public void initTest() {
        profile = createEntity(em);
    }

    @Test
    @Transactional
    public void createProfile() throws Exception {
        int databaseSizeBeforeCreate = profileRepository.findAll().size();

        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);
        restProfileMockMvc.perform(post("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isCreated());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeCreate + 1);
        Profile testProfile = profileList.get(profileList.size() - 1);
        assertThat(testProfile.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testProfile.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testProfile.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testProfile.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testProfile.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testProfile.getBio()).isEqualTo(DEFAULT_BIO);
        assertThat(testProfile.getFacebook()).isEqualTo(DEFAULT_FACEBOOK);
        assertThat(testProfile.getTwitter()).isEqualTo(DEFAULT_TWITTER);
        assertThat(testProfile.getLinkedin()).isEqualTo(DEFAULT_LINKEDIN);
        assertThat(testProfile.getInstagram()).isEqualTo(DEFAULT_INSTAGRAM);
        assertThat(testProfile.getOptionalsn()).isEqualTo(DEFAULT_OPTIONALSN);
        assertThat(testProfile.getBirthdate()).isEqualTo(DEFAULT_BIRTHDATE);
        assertThat(testProfile.getSibblings()).isEqualTo(DEFAULT_SIBBLINGS);
        assertThat(testProfile.isPet()).isEqualTo(DEFAULT_PET);
    }

    @Test
    @Transactional
    public void createProfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = profileRepository.findAll().size();

        // Create the Profile with an existing ID
        profile.setId(1L);
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfileMockMvc.perform(post("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = profileRepository.findAll().size();
        // set the field null
        profile.setCreationDate(null);

        // Create the Profile, which fails.
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        restProfileMockMvc.perform(post("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isBadRequest());

        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = profileRepository.findAll().size();
        // set the field null
        profile.setGender(null);

        // Create the Profile, which fails.
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        restProfileMockMvc.perform(post("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isBadRequest());

        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = profileRepository.findAll().size();
        // set the field null
        profile.setPhone(null);

        // Create the Profile, which fails.
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        restProfileMockMvc.perform(post("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isBadRequest());

        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProfiles() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList
        restProfileMockMvc.perform(get("/api/profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profile.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO.toString())))
            .andExpect(jsonPath("$.[*].facebook").value(hasItem(DEFAULT_FACEBOOK.toString())))
            .andExpect(jsonPath("$.[*].twitter").value(hasItem(DEFAULT_TWITTER.toString())))
            .andExpect(jsonPath("$.[*].linkedin").value(hasItem(DEFAULT_LINKEDIN.toString())))
            .andExpect(jsonPath("$.[*].instagram").value(hasItem(DEFAULT_INSTAGRAM.toString())))
            .andExpect(jsonPath("$.[*].optionalsn").value(hasItem(DEFAULT_OPTIONALSN.toString())))
            .andExpect(jsonPath("$.[*].birthdate").value(hasItem(DEFAULT_BIRTHDATE.toString())))
            .andExpect(jsonPath("$.[*].sibblings").value(hasItem(DEFAULT_SIBBLINGS)))
            .andExpect(jsonPath("$.[*].pet").value(hasItem(DEFAULT_PET.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get the profile
        restProfileMockMvc.perform(get("/api/profiles/{id}", profile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(profile.getId().intValue()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.bio").value(DEFAULT_BIO.toString()))
            .andExpect(jsonPath("$.facebook").value(DEFAULT_FACEBOOK.toString()))
            .andExpect(jsonPath("$.twitter").value(DEFAULT_TWITTER.toString()))
            .andExpect(jsonPath("$.linkedin").value(DEFAULT_LINKEDIN.toString()))
            .andExpect(jsonPath("$.instagram").value(DEFAULT_INSTAGRAM.toString()))
            .andExpect(jsonPath("$.optionalsn").value(DEFAULT_OPTIONALSN.toString()))
            .andExpect(jsonPath("$.birthdate").value(DEFAULT_BIRTHDATE.toString()))
            .andExpect(jsonPath("$.sibblings").value(DEFAULT_SIBBLINGS))
            .andExpect(jsonPath("$.pet").value(DEFAULT_PET.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllProfilesByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where creationDate equals to DEFAULT_CREATION_DATE
        defaultProfileShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the profileList where creationDate equals to UPDATED_CREATION_DATE
        defaultProfileShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllProfilesByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultProfileShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the profileList where creationDate equals to UPDATED_CREATION_DATE
        defaultProfileShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllProfilesByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where creationDate is not null
        defaultProfileShouldBeFound("creationDate.specified=true");

        // Get all the profileList where creationDate is null
        defaultProfileShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where gender equals to DEFAULT_GENDER
        defaultProfileShouldBeFound("gender.equals=" + DEFAULT_GENDER);

        // Get all the profileList where gender equals to UPDATED_GENDER
        defaultProfileShouldNotBeFound("gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllProfilesByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where gender in DEFAULT_GENDER or UPDATED_GENDER
        defaultProfileShouldBeFound("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER);

        // Get all the profileList where gender equals to UPDATED_GENDER
        defaultProfileShouldNotBeFound("gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllProfilesByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where gender is not null
        defaultProfileShouldBeFound("gender.specified=true");

        // Get all the profileList where gender is null
        defaultProfileShouldNotBeFound("gender.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where phone equals to DEFAULT_PHONE
        defaultProfileShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the profileList where phone equals to UPDATED_PHONE
        defaultProfileShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllProfilesByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultProfileShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the profileList where phone equals to UPDATED_PHONE
        defaultProfileShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllProfilesByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where phone is not null
        defaultProfileShouldBeFound("phone.specified=true");

        // Get all the profileList where phone is null
        defaultProfileShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByBioIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where bio equals to DEFAULT_BIO
        defaultProfileShouldBeFound("bio.equals=" + DEFAULT_BIO);

        // Get all the profileList where bio equals to UPDATED_BIO
        defaultProfileShouldNotBeFound("bio.equals=" + UPDATED_BIO);
    }

    @Test
    @Transactional
    public void getAllProfilesByBioIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where bio in DEFAULT_BIO or UPDATED_BIO
        defaultProfileShouldBeFound("bio.in=" + DEFAULT_BIO + "," + UPDATED_BIO);

        // Get all the profileList where bio equals to UPDATED_BIO
        defaultProfileShouldNotBeFound("bio.in=" + UPDATED_BIO);
    }

    @Test
    @Transactional
    public void getAllProfilesByBioIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where bio is not null
        defaultProfileShouldBeFound("bio.specified=true");

        // Get all the profileList where bio is null
        defaultProfileShouldNotBeFound("bio.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByFacebookIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where facebook equals to DEFAULT_FACEBOOK
        defaultProfileShouldBeFound("facebook.equals=" + DEFAULT_FACEBOOK);

        // Get all the profileList where facebook equals to UPDATED_FACEBOOK
        defaultProfileShouldNotBeFound("facebook.equals=" + UPDATED_FACEBOOK);
    }

    @Test
    @Transactional
    public void getAllProfilesByFacebookIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where facebook in DEFAULT_FACEBOOK or UPDATED_FACEBOOK
        defaultProfileShouldBeFound("facebook.in=" + DEFAULT_FACEBOOK + "," + UPDATED_FACEBOOK);

        // Get all the profileList where facebook equals to UPDATED_FACEBOOK
        defaultProfileShouldNotBeFound("facebook.in=" + UPDATED_FACEBOOK);
    }

    @Test
    @Transactional
    public void getAllProfilesByFacebookIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where facebook is not null
        defaultProfileShouldBeFound("facebook.specified=true");

        // Get all the profileList where facebook is null
        defaultProfileShouldNotBeFound("facebook.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByTwitterIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where twitter equals to DEFAULT_TWITTER
        defaultProfileShouldBeFound("twitter.equals=" + DEFAULT_TWITTER);

        // Get all the profileList where twitter equals to UPDATED_TWITTER
        defaultProfileShouldNotBeFound("twitter.equals=" + UPDATED_TWITTER);
    }

    @Test
    @Transactional
    public void getAllProfilesByTwitterIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where twitter in DEFAULT_TWITTER or UPDATED_TWITTER
        defaultProfileShouldBeFound("twitter.in=" + DEFAULT_TWITTER + "," + UPDATED_TWITTER);

        // Get all the profileList where twitter equals to UPDATED_TWITTER
        defaultProfileShouldNotBeFound("twitter.in=" + UPDATED_TWITTER);
    }

    @Test
    @Transactional
    public void getAllProfilesByTwitterIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where twitter is not null
        defaultProfileShouldBeFound("twitter.specified=true");

        // Get all the profileList where twitter is null
        defaultProfileShouldNotBeFound("twitter.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByLinkedinIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where linkedin equals to DEFAULT_LINKEDIN
        defaultProfileShouldBeFound("linkedin.equals=" + DEFAULT_LINKEDIN);

        // Get all the profileList where linkedin equals to UPDATED_LINKEDIN
        defaultProfileShouldNotBeFound("linkedin.equals=" + UPDATED_LINKEDIN);
    }

    @Test
    @Transactional
    public void getAllProfilesByLinkedinIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where linkedin in DEFAULT_LINKEDIN or UPDATED_LINKEDIN
        defaultProfileShouldBeFound("linkedin.in=" + DEFAULT_LINKEDIN + "," + UPDATED_LINKEDIN);

        // Get all the profileList where linkedin equals to UPDATED_LINKEDIN
        defaultProfileShouldNotBeFound("linkedin.in=" + UPDATED_LINKEDIN);
    }

    @Test
    @Transactional
    public void getAllProfilesByLinkedinIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where linkedin is not null
        defaultProfileShouldBeFound("linkedin.specified=true");

        // Get all the profileList where linkedin is null
        defaultProfileShouldNotBeFound("linkedin.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByInstagramIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where instagram equals to DEFAULT_INSTAGRAM
        defaultProfileShouldBeFound("instagram.equals=" + DEFAULT_INSTAGRAM);

        // Get all the profileList where instagram equals to UPDATED_INSTAGRAM
        defaultProfileShouldNotBeFound("instagram.equals=" + UPDATED_INSTAGRAM);
    }

    @Test
    @Transactional
    public void getAllProfilesByInstagramIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where instagram in DEFAULT_INSTAGRAM or UPDATED_INSTAGRAM
        defaultProfileShouldBeFound("instagram.in=" + DEFAULT_INSTAGRAM + "," + UPDATED_INSTAGRAM);

        // Get all the profileList where instagram equals to UPDATED_INSTAGRAM
        defaultProfileShouldNotBeFound("instagram.in=" + UPDATED_INSTAGRAM);
    }

    @Test
    @Transactional
    public void getAllProfilesByInstagramIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where instagram is not null
        defaultProfileShouldBeFound("instagram.specified=true");

        // Get all the profileList where instagram is null
        defaultProfileShouldNotBeFound("instagram.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByOptionalsnIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where optionalsn equals to DEFAULT_OPTIONALSN
        defaultProfileShouldBeFound("optionalsn.equals=" + DEFAULT_OPTIONALSN);

        // Get all the profileList where optionalsn equals to UPDATED_OPTIONALSN
        defaultProfileShouldNotBeFound("optionalsn.equals=" + UPDATED_OPTIONALSN);
    }

    @Test
    @Transactional
    public void getAllProfilesByOptionalsnIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where optionalsn in DEFAULT_OPTIONALSN or UPDATED_OPTIONALSN
        defaultProfileShouldBeFound("optionalsn.in=" + DEFAULT_OPTIONALSN + "," + UPDATED_OPTIONALSN);

        // Get all the profileList where optionalsn equals to UPDATED_OPTIONALSN
        defaultProfileShouldNotBeFound("optionalsn.in=" + UPDATED_OPTIONALSN);
    }

    @Test
    @Transactional
    public void getAllProfilesByOptionalsnIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where optionalsn is not null
        defaultProfileShouldBeFound("optionalsn.specified=true");

        // Get all the profileList where optionalsn is null
        defaultProfileShouldNotBeFound("optionalsn.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesByBirthdateIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where birthdate equals to DEFAULT_BIRTHDATE
        defaultProfileShouldBeFound("birthdate.equals=" + DEFAULT_BIRTHDATE);

        // Get all the profileList where birthdate equals to UPDATED_BIRTHDATE
        defaultProfileShouldNotBeFound("birthdate.equals=" + UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    public void getAllProfilesByBirthdateIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where birthdate in DEFAULT_BIRTHDATE or UPDATED_BIRTHDATE
        defaultProfileShouldBeFound("birthdate.in=" + DEFAULT_BIRTHDATE + "," + UPDATED_BIRTHDATE);

        // Get all the profileList where birthdate equals to UPDATED_BIRTHDATE
        defaultProfileShouldNotBeFound("birthdate.in=" + UPDATED_BIRTHDATE);
    }

    @Test
    @Transactional
    public void getAllProfilesByBirthdateIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where birthdate is not null
        defaultProfileShouldBeFound("birthdate.specified=true");

        // Get all the profileList where birthdate is null
        defaultProfileShouldNotBeFound("birthdate.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesBySibblingsIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where sibblings equals to DEFAULT_SIBBLINGS
        defaultProfileShouldBeFound("sibblings.equals=" + DEFAULT_SIBBLINGS);

        // Get all the profileList where sibblings equals to UPDATED_SIBBLINGS
        defaultProfileShouldNotBeFound("sibblings.equals=" + UPDATED_SIBBLINGS);
    }

    @Test
    @Transactional
    public void getAllProfilesBySibblingsIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where sibblings in DEFAULT_SIBBLINGS or UPDATED_SIBBLINGS
        defaultProfileShouldBeFound("sibblings.in=" + DEFAULT_SIBBLINGS + "," + UPDATED_SIBBLINGS);

        // Get all the profileList where sibblings equals to UPDATED_SIBBLINGS
        defaultProfileShouldNotBeFound("sibblings.in=" + UPDATED_SIBBLINGS);
    }

    @Test
    @Transactional
    public void getAllProfilesBySibblingsIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where sibblings is not null
        defaultProfileShouldBeFound("sibblings.specified=true");

        // Get all the profileList where sibblings is null
        defaultProfileShouldNotBeFound("sibblings.specified=false");
    }

    @Test
    @Transactional
    public void getAllProfilesBySibblingsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where sibblings greater than or equals to DEFAULT_SIBBLINGS
        defaultProfileShouldBeFound("sibblings.greaterOrEqualThan=" + DEFAULT_SIBBLINGS);

        // Get all the profileList where sibblings greater than or equals to (DEFAULT_SIBBLINGS + 1)
        defaultProfileShouldNotBeFound("sibblings.greaterOrEqualThan=" + (DEFAULT_SIBBLINGS + 1));
    }

    @Test
    @Transactional
    public void getAllProfilesBySibblingsIsLessThanSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where sibblings less than or equals to DEFAULT_SIBBLINGS
        defaultProfileShouldNotBeFound("sibblings.lessThan=" + DEFAULT_SIBBLINGS);

        // Get all the profileList where sibblings less than or equals to (DEFAULT_SIBBLINGS + 1)
        defaultProfileShouldBeFound("sibblings.lessThan=" + (DEFAULT_SIBBLINGS + 1));
    }


    @Test
    @Transactional
    public void getAllProfilesByPetIsEqualToSomething() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where pet equals to DEFAULT_PET
        defaultProfileShouldBeFound("pet.equals=" + DEFAULT_PET);

        // Get all the profileList where pet equals to UPDATED_PET
        defaultProfileShouldNotBeFound("pet.equals=" + UPDATED_PET);
    }

    @Test
    @Transactional
    public void getAllProfilesByPetIsInShouldWork() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where pet in DEFAULT_PET or UPDATED_PET
        defaultProfileShouldBeFound("pet.in=" + DEFAULT_PET + "," + UPDATED_PET);

        // Get all the profileList where pet equals to UPDATED_PET
        defaultProfileShouldNotBeFound("pet.in=" + UPDATED_PET);
    }

    @Test
    @Transactional
    public void getAllProfilesByPetIsNullOrNotNull() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        // Get all the profileList where pet is not null
        defaultProfileShouldBeFound("pet.specified=true");

        // Get all the profileList where pet is null
        defaultProfileShouldNotBeFound("pet.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProfileShouldBeFound(String filter) throws Exception {
        restProfileMockMvc.perform(get("/api/profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profile.getId().intValue())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].bio").value(hasItem(DEFAULT_BIO)))
            .andExpect(jsonPath("$.[*].facebook").value(hasItem(DEFAULT_FACEBOOK)))
            .andExpect(jsonPath("$.[*].twitter").value(hasItem(DEFAULT_TWITTER)))
            .andExpect(jsonPath("$.[*].linkedin").value(hasItem(DEFAULT_LINKEDIN)))
            .andExpect(jsonPath("$.[*].instagram").value(hasItem(DEFAULT_INSTAGRAM)))
            .andExpect(jsonPath("$.[*].optionalsn").value(hasItem(DEFAULT_OPTIONALSN)))
            .andExpect(jsonPath("$.[*].birthdate").value(hasItem(DEFAULT_BIRTHDATE.toString())))
            .andExpect(jsonPath("$.[*].sibblings").value(hasItem(DEFAULT_SIBBLINGS)))
            .andExpect(jsonPath("$.[*].pet").value(hasItem(DEFAULT_PET.booleanValue())));

        // Check, that the count call also returns 1
        restProfileMockMvc.perform(get("/api/profiles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProfileShouldNotBeFound(String filter) throws Exception {
        restProfileMockMvc.perform(get("/api/profiles?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProfileMockMvc.perform(get("/api/profiles/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingProfile() throws Exception {
        // Get the profile
        restProfileMockMvc.perform(get("/api/profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        int databaseSizeBeforeUpdate = profileRepository.findAll().size();

        // Update the profile
        Profile updatedProfile = profileRepository.findById(profile.getId()).get();
        // Disconnect from session so that the updates on updatedProfile are not directly saved in db
        em.detach(updatedProfile);
        updatedProfile
            .creationDate(UPDATED_CREATION_DATE)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .gender(UPDATED_GENDER)
            .phone(UPDATED_PHONE)
            .bio(UPDATED_BIO)
            .facebook(UPDATED_FACEBOOK)
            .twitter(UPDATED_TWITTER)
            .linkedin(UPDATED_LINKEDIN)
            .instagram(UPDATED_INSTAGRAM)
            .optionalsn(UPDATED_OPTIONALSN)
            .birthdate(UPDATED_BIRTHDATE)
            .sibblings(UPDATED_SIBBLINGS)
            .pet(UPDATED_PET);
        ProfileDTO profileDTO = profileMapper.toDto(updatedProfile);

        restProfileMockMvc.perform(put("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isOk());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeUpdate);
        Profile testProfile = profileList.get(profileList.size() - 1);
        assertThat(testProfile.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testProfile.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testProfile.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testProfile.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testProfile.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testProfile.getBio()).isEqualTo(UPDATED_BIO);
        assertThat(testProfile.getFacebook()).isEqualTo(UPDATED_FACEBOOK);
        assertThat(testProfile.getTwitter()).isEqualTo(UPDATED_TWITTER);
        assertThat(testProfile.getLinkedin()).isEqualTo(UPDATED_LINKEDIN);
        assertThat(testProfile.getInstagram()).isEqualTo(UPDATED_INSTAGRAM);
        assertThat(testProfile.getOptionalsn()).isEqualTo(UPDATED_OPTIONALSN);
        assertThat(testProfile.getBirthdate()).isEqualTo(UPDATED_BIRTHDATE);
        assertThat(testProfile.getSibblings()).isEqualTo(UPDATED_SIBBLINGS);
        assertThat(testProfile.isPet()).isEqualTo(UPDATED_PET);
    }

    @Test
    @Transactional
    public void updateNonExistingProfile() throws Exception {
        int databaseSizeBeforeUpdate = profileRepository.findAll().size();

        // Create the Profile
        ProfileDTO profileDTO = profileMapper.toDto(profile);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfileMockMvc.perform(put("/api/profiles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(profileDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Profile in the database
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProfile() throws Exception {
        // Initialize the database
        profileRepository.saveAndFlush(profile);

        int databaseSizeBeforeDelete = profileRepository.findAll().size();

        // Delete the profile
        restProfileMockMvc.perform(delete("/api/profiles/{id}", profile.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Profile> profileList = profileRepository.findAll();
        assertThat(profileList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Profile.class);
        Profile profile1 = new Profile();
        profile1.setId(1L);
        Profile profile2 = new Profile();
        profile2.setId(profile1.getId());
        assertThat(profile1).isEqualTo(profile2);
        profile2.setId(2L);
        assertThat(profile1).isNotEqualTo(profile2);
        profile1.setId(null);
        assertThat(profile1).isNotEqualTo(profile2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProfileDTO.class);
        ProfileDTO profileDTO1 = new ProfileDTO();
        profileDTO1.setId(1L);
        ProfileDTO profileDTO2 = new ProfileDTO();
        assertThat(profileDTO1).isNotEqualTo(profileDTO2);
        profileDTO2.setId(profileDTO1.getId());
        assertThat(profileDTO1).isEqualTo(profileDTO2);
        profileDTO2.setId(2L);
        assertThat(profileDTO1).isNotEqualTo(profileDTO2);
        profileDTO1.setId(null);
        assertThat(profileDTO1).isNotEqualTo(profileDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(profileMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(profileMapper.fromId(null)).isNull();
    }
}
