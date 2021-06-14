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
			if (repository.findRewardByRewardId(1L) == null)
			{
				Reward reward1 = new Reward("RV0001", " $10 Grocery Store Vouchers",
						"The vouchers can be redeemed at any Grocery Store outlet in Singapore" +
								". Valid for only one month from date of redemption", "Voucher",
						20);
				reward1.setRewardImageLink("https://images.unsplash.com/photo-1583258292688-d0213dc5a3a8?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8Z3JvY2VyeSUyMHN0b3JlfGVufDB8fDB8fA%3D%3D&ixlib=rb-1.2.1&w=1000&q=80");
				Reward reward2 = new Reward("RV0002", " $20 Grocery Store Vouchers",
						"The vouchers can be redeemed at any Grocery Store outlet in Singapore" +
								". Valid for only one month from date of redemption", "Voucher",
						40);
				reward2.setRewardImageLink("https://images.unsplash.com/photo-1583258292688-d0213dc5a3a8?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8Z3JvY2VyeSUyMHN0b3JlfGVufDB8fDB8fA%3D%3D&ixlib=rb-1.2.1&w=1000&q=80");
				Reward reward3 = new Reward("RV0003", " $10 Drinks Vouchers",
						"The vouchers can be redeemed at any Drinks outlet in Singapore" +
								". Valid for only one month from date of redemption", "Voucher",
						20);
				reward3.setRewardImageLink("https://images.pexels.com/photos/302899/pexels-photo-302899.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500");
				Reward reward4 = new Reward("RV0004", " $10 GrabFood Vouchers",
						"The vouchers can be redeemed when purchasing items from the GrabFood app" +
								"\n Valid for only one month from date of redemption", "Voucher",
						20);
				reward4.setRewardImageLink("https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxleHBsb3JlLWZlZWR8MXx8fGVufDB8fHx8&w=1000&q=80");
				Reward reward5 = new Reward("RV0005", "$20 Kinokuniya Vouchers",
						"The vouchers can be redeemed at any Kinokuniya outlet in Singapore" +
								"\n Valid for only three months from date of redemption", "Voucher",
						50);
				reward5.setRewardImageLink("https://images.unsplash.com/photo-1463320726281-696a485928c7?ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8Ym9va3N0b3JlfGVufDB8fDB8fA%3D%3D&ixlib=rb-1.2.1&w=1000&q=80");
				Reward reward6 = new Reward("RP0001", "Three Months Subscription to Fitness Plan",
						"Redeem this product to enjoy three months of free subscription to the Fitness Plan. " +
								" With this subscription, you can fulfil all your fitness goals!", "Product",
						50);
				reward6.setRewardImageLink("https://images.unsplash.com/photo-1571008887538-b36bb32f4571?ixid=MnwxMjA3fDB8MHxzZWFyY2h8M3x8cnVubmluZ3xlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&w=1000&q=80");
				Reward reward7 = new Reward("RP0002", "Fitness Tracker",
						"Use this fitness tracker to monitor your heartrate and keep count of the number of steps taken each day"
								+ ". You may redeem this product physically by going to the specified store address",
						"Product", 70);
				reward7.setStoreAddress("Singapore Mall, #01-27, Singapore 1234567");
				reward7.setRewardImageLink("https://www.kindpng.com/picc/m/648-6482354_fitness-tracker-png-apple-fitness-band-price-transparent.png");
				Reward reward8 = new Reward("RP0003", "Wireless Headphones",
						"These high-quality wireless headphones will provide you with the best experience for listening to your favourite songs/podcasts"
								+ ". You may redeem this product in the specified outlet store.",
						"Product", 150);
				reward8.setStoreAddress("Singapore Mall, #01-45, Singapore 1234567");
				reward8.setRewardImageLink("https://images.pexels.com/photos/3945667/pexels-photo-3945667.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500");
				Reward reward9 = new Reward("RP0004", "Three months subscription to Stock Market App",
						"Get three months of free subscription to the Stock Market App! Receive information on the latest market trends"
								+ "\n Make informed decisions on your own finances", "Product",
						50);
				reward9.setRewardImageLink("https://media.istockphoto.com/photos/stock-market-business-graph-chart-on-digital-screen-success-and-loss-picture-id1254779355?b=1&k=6&m=1254779355&s=170667a&w=0&h=U7q78jRYpTucnncFC7XAT2dH-MPuq2wOjKe5EIJnu9Y=");
				Reward reward10 = new Reward("RP0005", "Three months subscription to Career Guidance App",
						"Get three months of free subscription to the Career Guidance App! Receive valuable advice from career experts on how to land your dream job",
						"Product", 50);
				reward10.setRewardImageLink("https://images.unsplash.com/photo-1499750310107-5fef28a66643?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MzF8fGNhcmVlcnxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=600&q=60");

				repository.saveAll(Arrays.asList(reward1, reward2, reward3, reward4, reward5, reward6,
						reward7, reward8, reward9, reward10));
			}

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
