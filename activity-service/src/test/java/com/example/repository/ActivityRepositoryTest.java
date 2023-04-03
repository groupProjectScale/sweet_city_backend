package com.example.repository;

import com.example.BaseTest;

public class ActivityRepositoryTest extends BaseTest {
    /*@Autowired
    private ActivityRepository activityRepository;

    private static final String ACTIVITY_ID = UUID.randomUUID().toString();
    private static final String CREATOR_ID = UUID.randomUUID().toString();
    private static final UUID ACTIVITY_UUID = UUID.fromString(ACTIVITY_ID);
    private static final UUID CREATOR_UUID = UUID.fromString(CREATOR_ID);
    private static LocalDateTime now = LocalDateTime.now();
    private static final Timestamp ACTIVITY_START_TIME = Timestamp.valueOf(now.plusDays(1));
    private static final Timestamp ACTIVITY_END_TIME = Timestamp.valueOf(now.plusDays(2));
    private static final String NAME = "test";

    private Activity expectedActivity;


    @BeforeEach
    public void setup() {
        expectedActivity = new Activity(ACTIVITY_UUID,NAME,CREATOR_UUID,
                ACTIVITY_START_TIME, ACTIVITY_END_TIME);
    }

    @Test
    void testSave_returnSavedActivity() {
        Activity res = activityRepository.save(expectedActivity);

        assertThat(res).isNotNull();
        assertThat(res.getActivityId()).isNotNull();
        assertThat(res.getCurrentParticipants()).isEqualTo(1);
    }

     */
}
