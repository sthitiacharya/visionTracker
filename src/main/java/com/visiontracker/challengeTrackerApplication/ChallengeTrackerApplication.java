package com.visiontracker.challengeTrackerApplication;

import com.visiontracker.challengeTrackerApplication.models.db.Reward;
import com.visiontracker.challengeTrackerApplication.repositories.RewardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class ChallengeTrackerApplication {

	private static final Logger log = LoggerFactory.getLogger(ChallengeTrackerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ChallengeTrackerApplication.class, args);
	}
	@Bean
	public CommandLineRunner generateRewards(RewardRepository repository) {
		return (args) -> {
			// save a few rewards
			Reward reward1 = new Reward("RV0001", " $10 NTUC Fairprice Vouchers",
					"The vouchers can be redeemed at any NTUC Fairprice outlet in Singapore" +
							"\n Valid for only one month from date of redemption", "Voucher",
					20);
			Reward reward2 = new Reward("RV0002", " $20 NTUC Fairprice Vouchers",
					"The vouchers can be redeemed at any NTUC Fairprice outlet in Singapore" +
							"\n Valid for only one month from date of redemption", "Voucher",
					40);
			Reward reward3 = new Reward("RV0003", " $10 Starbucks Vouchers",
					"The vouchers can be redeemed at any Starbucks outlet in Singapore" +
							"\n Valid for only one month from date of redemption", "Voucher",
					20);
			Reward reward4 = new Reward("RV0004", " $10 GrabFood Vouchers",
					"The vouchers can be redeemed when purchasing items from the GrabFood app" +
							"\n Valid for only one month from date of redemption", "Voucher",
					20);
			Reward reward5 = new Reward("RV0005", "$20 Kinokuniya Vouchers",
					"The vouchers can be redeemed at any Kinokuniya outlet in Singapore" +
							"\n Valid for only three months from date of redemption", "Voucher",
					50);
			Reward reward6 = new Reward("RP0001", "Three Months Subscription to Fitness Plan",
					"Redeem this product to enjoy three months of free subscription to the Fitness Plan. " +
							" With this subscription, you can fulfil all your fitness goals!", "Product",
					50);
			Reward reward7 = new Reward("RP0002", "Fitness Tracker",
					"Use this fitness tracker to monitor your heartrate and keep count of the number of steps taken each day"
					+ "\n You may redeem this product physically by going to the specified store address",
					"Product", 70);
			reward7.setStoreAddress("Singapore Mall, #01-27, Singapore 1234567");
			Reward reward8 = new Reward("RP0003", "Wireless Headphones",
					"These high-quality wireless headphones will provide you with the best experience for listening to your favourite songs/podcasts"
					+ "\n You may redeem this product in the specified outlet store.",
					"Product", 150);
			reward8.setStoreAddress("Singapore Mall, #01-45, Singapore 1234567");
			Reward reward9 = new Reward("RP0004", "Three months subscription to Stock Market App",
					"Get three months of free subscription to the Stock Market App! Receive information on the latest market trends"
					+ "\n Make informed decisions on your own finances", "Product",
					50);
			Reward reward10 = new Reward("RP0005", "Three months subscription to Career Guidance App",
					"Get three months of free subscription to the Career Guidance App! Receive valuable advice from career experts on how to land your dream job",
					"Product", 50);

			repository.saveAll(Arrays.asList(reward1, reward2, reward3, reward4, reward5, reward6,
					reward7, reward8, reward9, reward10));

			// fetch all rewards
			log.info("Rewards found with findAll():");
			log.info("-------------------------------");
			for (Reward r : repository.findAll()) {
				log.info(r.toString());
			}
			log.info("");
		};
	}

}
