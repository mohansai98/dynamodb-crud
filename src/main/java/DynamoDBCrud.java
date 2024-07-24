import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.endpoints.internal.Value;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.Map;

public class DynamoDBCrud {

    private final DynamoDbClient dbClient;
    private final String tableName;

    public DynamoDBCrud(String tableName) {
        this.tableName = tableName;
        this.dbClient = DynamoDbClient.builder()
                .credentialsProvider(ProfileCredentialsProvider.create())
                .region(Region.US_EAST_2)
                .build();
    }

    public void createUser(String userId, String name, int age) {
        HashMap<String, AttributeValue> items = new HashMap<>();
        items.put("userId", AttributeValue.builder().s(userId).build());
        items.put("name", AttributeValue.builder().s(name).build());
        items.put("age", AttributeValue.builder().n(String.valueOf(age)).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(tableName)
                .item(items)
                .build();
        try {
            dbClient.putItem(request);
            System.out.println("User created successfully");
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void readuser(String userId) {
        HashMap<String, AttributeValue> key = new HashMap<>();
        key.put("userId", AttributeValue.builder().s(userId).build());

        GetItemRequest request = GetItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build();
        try {
            Map<String, AttributeValue> item = dbClient.getItem(request).item();
            if(item != null) {
                item.forEach((k, v) ->
                        System.out.println(k + ": " + v.toString()));
            }
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public void updateUser(String userId, String name, int age) {
        HashMap<String, AttributeValue> key = new HashMap<>();
        key.put("userId", AttributeValue.builder().s(userId).build());

        HashMap<String, AttributeValueUpdate> updateValues = new HashMap<>();
        updateValues.put("name", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().s(name).build())
                .action(AttributeAction.PUT)
                .build());
        updateValues.put("age", AttributeValueUpdate.builder()
                .value(AttributeValue.builder().n(String.valueOf(age)).build())
                .action(AttributeAction.PUT)
                .build());

        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .attributeUpdates(updateValues)
                .build();

        try {
            dbClient.updateItem(request);
            System.out.println("User updated successfully");
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }

    }

    public void deleteUser(String userId) {
        HashMap<String, AttributeValue> key = new HashMap<>();
        key.put("userId", AttributeValue.builder().s(userId).build());

        DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build();
        try {
            dbClient.deleteItem(request);
            System.out.println("User deleted successfully");
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    public static void main(String[] args) {
        String tableName = System.getenv("TABLE_NAME");
        DynamoDBCrud db = new DynamoDBCrud(tableName);
        db.createUser("101", "Mohan", 20);
        db.readuser("101");
        db.updateUser("101", "Mohan Sai", 26);
        db.readuser("101");
        db.deleteUser("101");
    }

}
