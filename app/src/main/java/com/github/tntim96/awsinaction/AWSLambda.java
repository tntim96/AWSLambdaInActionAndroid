package com.github.tntim96.awsinaction;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;


public interface AWSLambda {

  @LambdaFunction
  String greetingOnDemand(RequestClass request);

}
