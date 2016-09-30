//
//  RCTSodium.h
//
//  Created by Lyubomir Ivanov on 9/25/16.
//  Copyright © 2016 Lyubomir Ivanov. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface RCTSodium : NSObject <RCTBridgeModule>

- (void) sodium_version_string:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject;
- (void) crypto_box_keypair:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject;

@end

