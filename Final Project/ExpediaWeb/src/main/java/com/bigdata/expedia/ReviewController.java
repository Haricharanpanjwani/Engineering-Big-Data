package com.bigdata.expedia;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.bigdata.kafka.KafkaProducer;


/**
 * Handles requests for the application home page.
 */
@Controller
public class ReviewController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	
	@RequestMapping(value = "/contact.htm", method = RequestMethod.POST)
	public ModelAndView enterReview(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mv = new ModelAndView();
		mv.setViewName("review");

		return mv;
	}
	
	@RequestMapping(value = "/reviewSubmission.htm", method = RequestMethod.POST)
	public ModelAndView submitReview(HttpServletRequest request, HttpServletResponse response) throws SQLException {				

		ModelAndView mv= new ModelAndView();
		mv.setViewName("review");

		String hotelID = request.getParameter("hotelID");
		
		int id = Integer.parseInt(hotelID);
		
		String review = request.getParameter("review");
		
		System.out.println("Print Hotel ID: " + hotelID + " " + review);
		
		KafkaProducer kps = new KafkaProducer();
		
		String submit = null;
		submit = request.getParameter("submit");
		String positive = null;
		positive = request.getParameter("positive");
		String negative = null;
		negative = request.getParameter("negative");
		String checkScore = null;
		checkScore = request.getParameter("checkScore");
		
		System.out.println("In controller  " + submit + " " + positive + " " + negative);
		
		try {
			if(submit != null)
				kps.insertReview(id, review);
			else if(positive != null) {
				kps.generatePositiveReviews(id);
				mv.addObject("score" , kps.checkScore(id));
			}
			else if(negative != null) {
				kps.generateNegativeReviews(id);
				mv.addObject("score" , kps.checkScore(id));
			}
			else if(checkScore != null) {
				mv.addObject("score" , kps.checkScore(id));
			}
		}
		catch(Exception e) {
			System.out.println("Exception while calling kafka producer service");
			e.printStackTrace();
		}
		
		//kp.insertReview(hotelId, hotelReview);
		return mv;
	}

}
