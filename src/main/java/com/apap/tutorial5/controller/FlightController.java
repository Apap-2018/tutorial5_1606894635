package com.apap.tutorial5.controller;

import java.sql.Date;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.apap.tutorial5.model.FlightModel;
import com.apap.tutorial5.model.PilotModel;
import com.apap.tutorial5.service.FlightService;
import com.apap.tutorial5.service.PilotService;

@Controller
public class FlightController {
	@Autowired
	private FlightService flightService;
	
	@Autowired
	private PilotService pilotService;
	
	@RequestMapping(value = "/flight/add/{licenseNumber}", method = RequestMethod.GET)
	private String add(@PathVariable("licenseNumber") String licenseNumber, Model model) {
		PilotModel pilot = pilotService.getPilotDetailByLicenseNumber(licenseNumber);
		ArrayList<FlightModel> flightList = new ArrayList<>();
		pilot.setPilotFlight(flightList);
		flightList.add(new FlightModel());
		model.addAttribute("pilot", pilot);
		model.addAttribute("title", "Add Flight");
		return "addFlight";
	}	
	
	@RequestMapping(value = "/flight/add/{licenseNumber}", method = RequestMethod.POST, params={"save"})
	private String addFligthSubmit(@ModelAttribute PilotModel pilot, Model model) {
		PilotModel archive = pilotService.getPilotDetailByLicenseNumber(pilot.getLicenseNumber());
		for(FlightModel flight : pilot.getPilotFlight()) {
			flight.setPilot(archive);
			flightService.addFlight(flight);
		}
		model.addAttribute("title", "APAP");
		return "add";
	}
	
	@RequestMapping(value="/flight/add/{licenseNumber}", params={"removeRow"}, method = RequestMethod.POST)
	public String removeRow(PilotModel pilot, BindingResult bindingResult, HttpServletRequest req, Model model) {
	   Integer rowId = Integer.valueOf(req.getParameter("removeRow"));
	   pilot.getPilotFlight().remove(rowId.intValue());
	   model.addAttribute("pilot", pilot);
	   return "addFlight";
	}	
	
	@RequestMapping(value="/flight/add/{licenseNumber}", params={"addRow"}, method = RequestMethod.POST)
	public String addRow(PilotModel pilot, BindingResult bindingResult, Model model) {
		if (pilot.getPilotFlight() ==  null) {
			pilot.setPilotFlight(new ArrayList<FlightModel>());
		}
		pilot.getPilotFlight().add(new FlightModel());
	    model.addAttribute("pilot", pilot);
	    return "addFlight";
	}
	
	@RequestMapping(value = "/flight/add", method = RequestMethod.POST)
	private String addFlightSubmit(@ModelAttribute FlightModel flight, Model model) {
		flightService.addFlight(flight);
		model.addAttribute("title", "Add Flight");
		return "add";
	}
	
	@RequestMapping(value = "/flight/delete", method = RequestMethod.POST)
	private String deleteFlight(@ModelAttribute PilotModel pilot, Model model) {
		for(FlightModel flight : pilot.getPilotFlight()) {
			flightService.deleteFlight(flight.getId());
		}
		model.addAttribute("title", "Delete Flight");
		return "delete";
	}
	
	@RequestMapping(value = "/flight/update/{licenseNumber}/{id}", method = RequestMethod.GET)
	private String updateFlight(@PathVariable(value = "licenseNumber") String licenseNumber,@PathVariable(value="id") long id, Model model) {
		FlightModel flight= flightService.findFlight(id);
		model.addAttribute("flight", flight);
		model.addAttribute("title", "Update Flight");
		return "updateFlight";
	}
	
	@RequestMapping(value = "flight/update", method = RequestMethod.POST)
	private String updateFlightDB(Model model, @RequestParam(value = "id") long id, @RequestParam(value = "flightNumber") String flightNumber, @RequestParam(value = "origin") String origin, @RequestParam(value = "destination") String destination, @RequestParam(value = "time") Date time) {
		flightService.updateFlight(id, flightNumber, origin, destination, time);
		model.addAttribute("title", "Update Flight");
		return "update";
	}
	
	@RequestMapping(value = "flight/view")
	private String viewFlight(Model model) {
		model.addAttribute("listOfFlight", flightService.findAllFlight());
		model.addAttribute("title", "View All Flight");
		return "viewFlight";
	}
	
	@RequestMapping(value = "flight/view/detail/{licenseNumber}")
	private String viewDetailFlight(Model model, @PathVariable(value = "licenseNumber") String licenseNumber) {
		model.addAttribute("pilot", pilotService.getPilotDetailByLicenseNumber(licenseNumber));
		model.addAttribute("title", "View Detail Flight");
		return "viewDetailFlight";
	}
}
