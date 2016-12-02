# callcongress

Alexa skill to help people find out about current legislation and contact their congresspeople.

## Use

Alexa, ask call congress
- what are some upcoming bills? (you can say “no” or “next” to cycle through the list of bills)
- set my zip to <zipcode>
- call my representative


## Deployment

### Build

> lein uberjar
### Upload to AWS Lambda

- https://console.aws.amazon.com/lambda/home?region=us-east-1#/functions/CallCongress/$LATEST?tab=code
- ARN - arn:aws:lambda:us-east-1:316385792873:function:CallCongress:$LATEST

### Upload interaction model to AWS

### Test out on Alexa

https://developer.amazon.com/edw/home.html#/skill/amzn1.ask.skill.211aa2b8-b06f-4e2b-bdc1-8b0fa87673cd/en_US/testing

### Database

An AWS DynamoDB table is used to store zip codes.

- https://console.aws.amazon.com/dynamodb/home?region=us-east-1#tables:selected=congress_user_prefs
- arn:aws:dynamodb:us-east-1:316385792873:table/congress_user_prefs

## License

Copyright © 2016 Michael Travers

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
