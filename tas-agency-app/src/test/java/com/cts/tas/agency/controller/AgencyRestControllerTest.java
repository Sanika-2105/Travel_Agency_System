package com.cts.tas.agency.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cts.tas.agency.dto.AgencyDTO;
import com.cts.tas.agency.service.AgencyService;
@ExtendWith(MockitoExtension.class)
class AgencyRestControllerTest {
	
	@Mock
    private AgencyService agencyService;

    @InjectMocks
    private AgencyRestController agencyController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(agencyController).build();
    }

    //test for get all agencies
    @Test
    void testGetAllAgencies() throws Exception {
        AgencyDTO agency1 = new AgencyDTO(1L, "Sky High Travels", "sky@agency.com", "Hyderabad");
        AgencyDTO agency2 = new AgencyDTO(2L, "Mountain Tours", "mountain@agency.com", "Delhi");

        when(agencyService.getAllAgencies()).thenReturn(Arrays.asList(agency1, agency2));

        mockMvc.perform(get("/agencies"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].name").value("Sky High Travels"))
               .andExpect(jsonPath("$[1].name").value("Mountain Tours"));
    }
    //test for get agency by id
    @Test
    void testGetAgencyById() throws Exception {
        setup();
        AgencyDTO agency = new AgencyDTO(1L, "Sky High Travels", "sky@agency.com", "Hyderabad");

        when(agencyService.getAgencyById(1L)).thenReturn(agency);

        mockMvc.perform(get("/agencies/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.name").value("Sky High Travels"))
               .andExpect(jsonPath("$.contactEmail").value("sky@agency.com"));
    }
    //test for create new agency
    @Test
    void testCreateAgency() throws Exception {
        setup();
        AgencyDTO response = new AgencyDTO(1L, "Sky High Travels", "sky@agency.com", "Hyderabad");

        when(agencyService.createAgency(any(AgencyDTO.class))).thenReturn(response);

        mockMvc.perform(post("/agencies")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Sky High Travels\",\"contactEmail\":\"sky@agency.com\",\"address\":\"Hyderabad\"}"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.name").value("Sky High Travels"));
    }
    //test for updating agency
    @Test
    void testUpdateAgency() throws Exception {
        AgencyDTO updated = new AgencyDTO(1L, "Sky High Travels Updated", "sky@agency.com", "Hyderabad");

        when(agencyService.updateAgency(eq(1L), any(AgencyDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/agencies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Sky High Travels Updated\",\"contactEmail\":\"sky@agency.com\",\"address\":\"Hyderabad\"}"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.name").value("Sky High Travels Updated"))
               .andExpect(jsonPath("$.contactEmail").value("sky@agency.com"))
               .andExpect(jsonPath("$.address").value("Hyderabad"));
    }
    //test for delete agency
    @Test
    void testDeleteAgency() throws Exception {
        doNothing().when(agencyService).deleteAgency(1L);

        mockMvc.perform(delete("/agencies/1"))
               .andExpect(status().isOk())
               .andExpect(content().string("Agency with id 1 deleted successfully"));
    }
    
}
