# Define variables for table name and attribute names
PARTICIPANTS_STATE_TABLE_NAME="participants_state_table"
LIVE_PARTICIPANTS_TABLE_NAME="live_participants_table"
USER_ID_ATTRIBUTE_NAME="user_uuid"
ACTIVITY_UUID_ATTRIBUTE_NAME="activity_uuid"
STATE_ATTRIBUTE_NAME="participant_state"
NUM_PARTICIPANTS_ATTRIBUTE_NAME="number_of_participants"

# Create the participants state table with a composite primary key
awslocal dynamodb create-table \
  --table-name $PARTICIPANTS_STATE_TABLE_NAME \
  --attribute-definitions \
    AttributeName=$USER_ID_ATTRIBUTE_NAME,AttributeType=S \
    AttributeName=$ACTIVITY_UUID_ATTRIBUTE_NAME,AttributeType=S \
  --key-schema \
    AttributeName=$ACTIVITY_UUID_ATTRIBUTE_NAME,KeyType=HASH \
    AttributeName=$USER_ID_ATTRIBUTE_NAME,KeyType=RANGE \
  --provisioned-throughput \
    ReadCapacityUnits=5,WriteCapacityUnits=5

# Wait for the participants state table to be created before proceeding
awslocal dynamodb wait table-exists --table-name $PARTICIPANTS_STATE_TABLE_NAME

# Create the live participants table with a primary key and a global secondary index
awslocal dynamodb create-table \
  --table-name $LIVE_PARTICIPANTS_TABLE_NAME \
  --attribute-definitions \
    AttributeName=$ACTIVITY_UUID_ATTRIBUTE_NAME,AttributeType=S \
    AttributeName=$NUM_PARTICIPANTS_ATTRIBUTE_NAME,AttributeType=N \
  --key-schema \
    AttributeName=$ACTIVITY_UUID_ATTRIBUTE_NAME,KeyType=HASH \
    AttributeName=$NUM_PARTICIPANTS_ATTRIBUTE_NAME,KeyType=RANGE \
  --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \

# Wait for the live participants table to be created before proceeding
awslocal dynamodb wait table-exists --table-name $LIVE_PARTICIPANTS_TABLE_NAME

# Insert an item into the participants state table
awslocal dynamodb put-item \
  --table-name $PARTICIPANTS_STATE_TABLE_NAME \
  --item '{"activity_uuid": {"S": "activity1"}, "user_uuid": {"S": "user1"}, "participant_state": {"S": "joined"}}'
awslocal dynamodb put-item \
    --table-name $PARTICIPANTS_STATE_TABLE_NAME \
    --item '{"activity_uuid": {"S": "activity1"}, "user_uuid": {"S": "user2"}, "participant_state": {"S": "joined"}}'
awslocal dynamodb put-item \
  --table-name $LIVE_PARTICIPANTS_TABLE_NAME \
  --item '{"activity_uuid": {"S": "activity1"}, "number_of_participants": {"N": "2"}}'

awslocal s3 mb s3://sweet-city-storage
awslocal sqs create-queue --queue-name activity-sqs
