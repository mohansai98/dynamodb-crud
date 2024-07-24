package com.myorg;

import software.amazon.awscdk.CfnOutput;
import software.amazon.awscdk.CfnOutputProps;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.BillingMode;
import software.amazon.awscdk.services.dynamodb.Table;
import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;


public class DynamodbCrudStack extends Stack {
    public DynamodbCrudStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public DynamodbCrudStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        // The code that defines your stack goes here

        Table table = Table.Builder.create(this, "UsersTable")
                .tableName("UsersTable")
                .partitionKey(Attribute.builder()
                        .name("userId")
                        .type(AttributeType.STRING)
                        .build())
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .build();

        new CfnOutput(this, "TableName", CfnOutputProps.builder()
                .description("The name of the DynamoDB table")
                .value(table.getTableName())
                .build());

    }
}
