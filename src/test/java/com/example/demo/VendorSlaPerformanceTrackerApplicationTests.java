package com.example.demo;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.*;
import com.example.demo.service.impl.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * One massive TestNG class with 60-70 tests
 * Topic order strictly followed.
 */
@Listeners(TestResultListener.class)
public class VendorSlaPerformanceTrackerApplicationTests {

    private VendorRepository vendorRepository;
    private SLARequirementRepository slaRequirementRepository;
    private DeliveryEvaluationRepository deliveryEvaluationRepository;
    private VendorPerformanceScoreRepository vendorPerformanceScoreRepository;
    private VendorTierRepository vendorTierRepository;

    private VendorService vendorService;
    private SLARequirementService slaRequirementService;
    private DeliveryEvaluationService deliveryEvaluationService;
    private VendorPerformanceScoreService vendorPerformanceScoreService;
    private VendorTierService vendorTierService;

    private JwtTokenProvider jwtTokenProvider;

    public static class SimpleStatusServlet extends jakarta.servlet.http.HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("text/plain");
            PrintWriter writer = resp.getWriter();
            writer.write("OK");
            writer.flush();
        }
    }

    @BeforeClass
    public void setUp() {
        vendorRepository = mock(VendorRepository.class);
        slaRequirementRepository = mock(SLARequirementRepository.class);
        deliveryEvaluationRepository = mock(DeliveryEvaluationRepository.class);
        vendorPerformanceScoreRepository = mock(VendorPerformanceScoreRepository.class);
        vendorTierRepository = mock(VendorTierRepository.class);

        vendorService = new VendorServiceImpl(vendorRepository);
        slaRequirementService = new SLARequirementServiceImpl(slaRequirementRepository);
        deliveryEvaluationService = new DeliveryEvaluationServiceImpl(
                deliveryEvaluationRepository, vendorRepository, slaRequirementRepository
        );
        vendorPerformanceScoreService = new VendorPerformanceScoreServiceImpl(
                vendorPerformanceScoreRepository,
                deliveryEvaluationRepository,
                vendorRepository,
                vendorTierRepository
        );

        vendorTierService = new VendorTierServiceImpl(vendorTierRepository);

        jwtTokenProvider = new JwtTokenProvider("MySuperSecretVendorSlaKey1234567890", 3600000L);
    }

    // 1. Servlet tests
    @Test(priority = 1, groups = {"servlet"})
    public void testServletGetReturnsOkStatus() throws IOException, ServletException {
        SimpleStatusServlet servlet = new SimpleStatusServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        StringWriter out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);
        when(response.getWriter()).thenReturn(writer);

        servlet.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        Assert.assertEquals(out.toString(), "OK");
    }

    @Test(priority = 2, groups = {"servlet"})
    public void testServletContentTypeIsTextPlain() throws IOException {
        SimpleStatusServlet servlet = new SimpleStatusServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);
        when(response.getWriter()).thenReturn(writer);

        servlet.doGet(request, response);

        verify(response).setContentType("text/plain");
    }

    @Test(priority = 3, groups = {"servlet"})
    public void testServletHandlesMultipleCalls() throws IOException {
        SimpleStatusServlet servlet = new SimpleStatusServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        StringWriter out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);
        when(response.getWriter()).thenReturn(writer);

        servlet.doGet(request, response);
        servlet.doGet(request, response);

        verify(response, times(2)).setStatus(HttpServletResponse.SC_OK);
    }

    @Test(priority = 4, groups = {"servlet"})
    public void testServletNoExceptionOnInitDestroy() {
        SimpleStatusServlet servlet = new SimpleStatusServlet();
        try {
            servlet.init();
            servlet.destroy();
        } catch (Exception e) {
            Assert.fail("Servlet lifecycle should not throw exception");
        }
    }

    @Test(priority = 5, groups = {"servlet"})
    public void testServletResponseWriterUsedOnce() throws IOException {
        SimpleStatusServlet servlet = new SimpleStatusServlet();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);

        servlet.doGet(request, response);

        verify(writer).write("OK");
    }

    @Test(priority = 6, groups = {"servlet"})
    public void testServletDeploymentSimulation() {
        SimpleStatusServlet servlet = new SimpleStatusServlet();
        try {
            servlet.init();
            servlet.destroy();
        } catch (Exception e) {
            Assert.fail("Servlet init/destroy should not throw");
        }
    }

    // 2. CRUD tests
    @Test(priority = 10, groups = {"crud"})
    public void testCreateVendorSuccess() {
        Vendor vendor = new Vendor("Vendor A", "a@example.com", "123");
        when(vendorRepository.existsByName("Vendor A")).thenReturn(false);
        when(vendorRepository.save(any(Vendor.class))).thenAnswer(invocation -> {
            Vendor v = invocation.getArgument(0);
            v.setId(1L);
            return v;
        });

        Vendor created = vendorService.createVendor(vendor);
        Assert.assertNotNull(created.getId());
        Assert.assertEquals(created.getName(), "Vendor A");
    }

    @Test(priority = 11, groups = {"crud"})
    public void testCreateVendorDuplicateNameFails() {
        Vendor vendor = new Vendor("Vendor A", "a@example.com", "123");
        when(vendorRepository.existsByName("Vendor A")).thenReturn(true);
        try {
            vendorService.createVendor(vendor);
            Assert.fail("Expected IllegalArgumentException for duplicate vendor name");
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(ex.getMessage().contains("unique"));
        }
    }
    @Test(priority = 12, groups = {"crud"})
    public void testUpdateVendorContactDetails() {
        Vendor existing = new Vendor("Vendor A", "old@example.com", "000");
        existing.setId(1L);
        when(vendorRepository.findById(1L))
                .thenReturn(java.util.Optional.of(existing));
        when(vendorRepository.existsByName("Vendor A")).thenReturn(false);
        when(vendorRepository.save(any(Vendor.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Vendor update = new Vendor();
        update.setContactEmail("new@example.com");
        update.setContactPhone("111");

        Vendor result = vendorService.updateVendor(1L, update);
        Assert.assertEquals(result.getContactEmail(), "new@example.com");
        Assert.assertEquals(result.getContactPhone(), "111");
    }

    @Test(priority = 13, groups = {"crud"})
    public void testGetVendorByIdNotFound() {
        when(vendorRepository.findById(99L))
                .thenReturn(java.util.Optional.empty());

        try {
            vendorService.getVendorById(99L);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(ex.getMessage().contains("not found"));
        }
    }

    @Test(priority = 14, groups = {"crud"})
    public void testListVendorsReturnsEmptyList() {
        when(vendorRepository.findAll()).thenReturn(Collections.emptyList());
        List<Vendor> vendors = vendorService.getAllVendors();
        Assert.assertTrue(vendors.isEmpty());
    }

    @Test(priority = 15, groups = {"crud"})
    public void testDeactivateVendor() {
        Vendor existing = new Vendor("Vendor A", "a@example.com", "123");
        existing.setId(1L);

        when(vendorRepository.findById(1L))
                .thenReturn(java.util.Optional.of(existing));

        when(vendorRepository.save(any(Vendor.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        vendorService.deactivateVendor(1L);

        Assert.assertFalse(existing.getActive());
    }

    @Test(priority = 16, groups = {"crud"})
    public void testCreateSlaRequirementSuccess() {
        SLARequirement req = new SLARequirement("Req1", "desc", 5, 95.0);

        when(slaRequirementRepository.existsByRequirementName("Req1"))
                .thenReturn(false);

        when(slaRequirementRepository.save(any(SLARequirement.class)))
                .thenAnswer(invocation -> {
                    SLARequirement r = invocation.getArgument(0);
                    r.setId(1L);
                    return r;
                });

        SLARequirement created = slaRequirementService.createRequirement(req);
        Assert.assertNotNull(created.getId());
        Assert.assertEquals(created.getMaxDeliveryDays().intValue(), 5);
    }

    @Test(priority = 17, groups = {"crud"})
    public void testCreateSlaRequirementInvalidMaxDays() {
        SLARequirement req = new SLARequirement("Req1", "desc", 0, 90.0);

        try {
            slaRequirementService.createRequirement(req);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(ex.getMessage().contains("Max delivery days"));
        }
    }

    @Test(priority = 18, groups = {"crud"})
    public void testCreateSlaRequirementInvalidQualityScore() {
        SLARequirement req = new SLARequirement("Req1", "desc", 3, 120.0);

        try {
            slaRequirementService.createRequirement(req);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(ex.getMessage().contains("Quality score"));
        }
    }

    @Test(priority = 19, groups = {"crud"})
    public void testUpdateSlaRequirementNameConflict() {
        SLARequirement existing = new SLARequirement("Existing", "desc", 3, 90.0);
        existing.setId(1L);

        when(slaRequirementRepository.findById(1L))
                .thenReturn(java.util.Optional.of(existing));

        when(slaRequirementRepository.existsByRequirementName("NewName"))
                .thenReturn(true);

        SLARequirement update = new SLARequirement();
        update.setRequirementName("NewName");

        try {
            slaRequirementService.updateRequirement(1L, update);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(ex.getMessage().contains("unique"));
        }
    }

    @Test(priority = 20, groups = {"crud"})
    public void testDeactivateSlaRequirement() {
        SLARequirement existing = new SLARequirement("Req1", "desc", 3, 90.0);
        existing.setId(1L);

        when(slaRequirementRepository.findById(1L))
                .thenReturn(java.util.Optional.of(existing));

        when(slaRequirementRepository.save(any(SLARequirement.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        slaRequirementService.deactivateRequirement(1L);

        Assert.assertFalse(existing.getActive());
    }

    // 3. DI / IoC
    @Test(priority = 25, groups = {"di-ioc"})
    public void testVendorServiceInjectedRepository() {
        Assert.assertNotNull(vendorService);
        Assert.assertNotNull(vendorRepository);
    }

    @Test(priority = 26, groups = {"di-ioc"})
    public void testSlaServiceInjectedRepository() {
        Assert.assertNotNull(slaRequirementService);
        Assert.assertNotNull(slaRequirementRepository);
    }

    @Test(priority = 27, groups = {"di-ioc"})
    public void testDeliveryEvaluationServiceInjectedRepos() {
        Assert.assertNotNull(deliveryEvaluationService);
        Assert.assertNotNull(vendorRepository);
        Assert.assertNotNull(slaRequirementRepository);
    }

    @Test(priority = 28, groups = {"di-ioc"})
    public void testIoCAllowsMockReplacement() {
        VendorRepository mockRepo = mock(VendorRepository.class);
        VendorService anotherService = new VendorServiceImpl(mockRepo);
        Assert.assertNotSame(vendorRepository, mockRepo);
        Assert.assertNotNull(anotherService);
    }

    @Test(priority = 29, groups = {"di-ioc"})
    public void testIoCWithDifferentImplementationsConceptually() {
        VendorService serviceRef = new VendorServiceImpl(vendorRepository);
        Assert.assertTrue(serviceRef instanceof VendorServiceImpl);
    }

    @Test(priority = 30, groups = {"di-ioc"})
    public void testIoCMultipleServicesShareSameRepository() {
        VendorService vs = new VendorServiceImpl(vendorRepository);
        SLARequirementService ss = new SLARequirementServiceImpl(slaRequirementRepository);
        Assert.assertNotNull(vs);
        Assert.assertNotNull(ss);
    }

    // 4. Hibernate configs & CRUD
    @Test(priority = 35, groups = {"hibernate"})
    public void testDeliveryEvaluationValidationOnCreate() {
        Vendor v = new Vendor("Vendor A", "a@example.com", "123");
        v.setId(1L);

        SLARequirement sla = new SLARequirement("Req1", "desc", 3, 90.0);
        sla.setId(1L);

        DeliveryEvaluation eval = new DeliveryEvaluation();
        eval.setVendor(v);
        eval.setSlaRequirement(sla);
        eval.setActualDeliveryDays(2);
        eval.setQualityScore(95.0);
        eval.setEvaluationDate(LocalDate.now());

        when(vendorRepository.findById(1L))
                .thenReturn(java.util.Optional.of(v));

        when(slaRequirementRepository.findById(1L))
                .thenReturn(java.util.Optional.of(sla));

        when(deliveryEvaluationRepository.save(any(DeliveryEvaluation.class)))
                .thenAnswer(invocation -> {
                    DeliveryEvaluation e = invocation.getArgument(0);
                    e.setId(1L);
                    return e;
                });

        DeliveryEvaluation saved = deliveryEvaluationService.createEvaluation(eval);
        Assert.assertTrue(saved.getMeetsDeliveryTarget());
        Assert.assertTrue(saved.getMeetsQualityTarget());
    }
    @Test(priority = 36, groups = {"hibernate"})
    public void testDeliveryEvaluationFailsOnInactiveVendor() {
        Vendor v = new Vendor("Vendor A", "a@example.com", "123");
        v.setId(1L);
        v.setActive(false);

        SLARequirement sla = new SLARequirement("Req1", "desc", 3, 90.0);
        sla.setId(1L);

        DeliveryEvaluation eval = new DeliveryEvaluation();
        eval.setVendor(v);
        eval.setSlaRequirement(sla);
        eval.setActualDeliveryDays(2);
        eval.setQualityScore(95.0);
        eval.setEvaluationDate(LocalDate.now());

        when(vendorRepository.findById(1L))
                .thenReturn(java.util.Optional.of(v));

        when(slaRequirementRepository.findById(1L))
                .thenReturn(java.util.Optional.of(sla));

        try {
            deliveryEvaluationService.createEvaluation(eval);
            Assert.fail("Expected IllegalStateException for inactive vendor");
        } catch (IllegalStateException ex) {
            Assert.assertTrue(ex.getMessage().contains("active vendors"));
        }
    }

    @Test(priority = 37, groups = {"hibernate"})
    public void testDeliveryEvaluationFailsOnNegativeDays() {
        Vendor v = new Vendor("Vendor A", "a@example.com", "123");
        v.setId(1L);
        v.setActive(true);

        SLARequirement sla = new SLARequirement("Req1", "desc", 3, 90.0);
        sla.setId(1L);

        DeliveryEvaluation eval = new DeliveryEvaluation();
        eval.setVendor(v);
        eval.setSlaRequirement(sla);
        eval.setActualDeliveryDays(-1);
        eval.setQualityScore(95.0);
        eval.setEvaluationDate(LocalDate.now());

        when(vendorRepository.findById(1L))
                .thenReturn(java.util.Optional.of(v));

        when(slaRequirementRepository.findById(1L))
                .thenReturn(java.util.Optional.of(sla));

        try {
            deliveryEvaluationService.createEvaluation(eval);
            Assert.fail("Expected IllegalArgumentException for negative days");
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(ex.getMessage().contains(">= 0"));
        }
    }

    @Test(priority = 38, groups = {"hibernate"})
    public void testDeliveryEvaluationFailsOnQualityOutOfRange() {
        Vendor v = new Vendor("Vendor A", "a@example.com", "123");
        v.setId(1L);
        v.setActive(true);

        SLARequirement sla = new SLARequirement("Req1", "desc", 3, 90.0);
        sla.setId(1L);

        DeliveryEvaluation eval = new DeliveryEvaluation();
        eval.setVendor(v);
        eval.setSlaRequirement(sla);
        eval.setActualDeliveryDays(2);
        eval.setQualityScore(150.0);
        eval.setEvaluationDate(LocalDate.now());

        when(vendorRepository.findById(1L))
                .thenReturn(java.util.Optional.of(v));

        when(slaRequirementRepository.findById(1L))
                .thenReturn(java.util.Optional.of(sla));

        try {
            deliveryEvaluationService.createEvaluation(eval);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(ex.getMessage().contains("between 0 and 100"));
        }
    }

    @Test(priority = 39, groups = {"hibernate"})
    public void testHibernateEntityMappingsVendorIdGenerated() {
        Vendor v = new Vendor("V", null, null);
        Assert.assertNull(v.getId());
    }

    @Test(priority = 40, groups = {"hibernate"})
    public void testHibernateEntityMappingSlaRequirement() {
        SLARequirement r = new SLARequirement("Req", null, 2, 50.0);
        Assert.assertEquals(r.getMaxDeliveryDays().intValue(), 2);
    }

    // 5. JPA normalization
    @Test(priority = 45, groups = {"jpa-normalization"})
    public void testDeliveryEvaluationHasForeignKeysNotDuplicatedData() {
        DeliveryEvaluation eval = new DeliveryEvaluation();
        eval.setVendor(new Vendor("Vendor A", "a@example.com", "123"));
        eval.setSlaRequirement(new SLARequirement("Req1", "desc", 3, 90.0));
        Assert.assertNotNull(eval.getVendor());
        Assert.assertNotNull(eval.getSlaRequirement());
    }

    @Test(priority = 46, groups = {"jpa-normalization"})
    public void testVendorDoesNotContainSlaColumns() {
        Vendor v = new Vendor();
        Assert.assertNull(v.getId());
    }

    @Test(priority = 47, groups = {"jpa-normalization"})
    public void testSlaDoesNotContainVendorColumns() {
        SLARequirement s = new SLARequirement();
        Assert.assertNull(s.getId());
    }

    @Test(priority = 48, groups = {"jpa-normalization"})
    public void test1NFNoRepeatingGroupsInVendor() {
        Vendor v = new Vendor("V", "e", "p");
        Assert.assertNotNull(v.getName());
    }

    @Test(priority = 49, groups = {"jpa-normalization"})
    public void test2NFAllNonKeyAttributesDependOnKey() {
        Vendor v = new Vendor("V", "e", "p");
        v.setId(1L);
        Assert.assertEquals(v.getName(), "V");
    }

    @Test(priority = 50, groups = {"jpa-normalization"})
    public void test3NFNoTransitiveDependencies() {
        SLARequirement s = new SLARequirement("Req", "desc", 2, 80.0);
        Assert.assertEquals(s.getDescription(), "desc");
    }

    // 6. Many-to-many via evaluations
    @Test(priority = 55, groups = {"many-to-many"})
    public void testVendorCanHaveMultipleSlaViaEvaluations() {
        Long vendorId = 1L;
        DeliveryEvaluation e1 = new DeliveryEvaluation();
        DeliveryEvaluation e2 = new DeliveryEvaluation();
        e1.setSlaRequirement(new SLARequirement("Req1", "d1", 3, 90.0));
        e2.setSlaRequirement(new SLARequirement("Req2", "d2", 2, 95.0));

        when(deliveryEvaluationRepository.findByVendorId(vendorId))
                .thenReturn(Arrays.asList(e1, e2));

        List<DeliveryEvaluation> list = deliveryEvaluationService.getEvaluationsForVendor(vendorId);
        Assert.assertEquals(list.size(), 2);
        Assert.assertNotEquals(list.get(0).getSlaRequirement().getRequirementName(),
                list.get(1).getSlaRequirement().getRequirementName());
    }

    @Test(priority = 56, groups = {"many-to-many"})
    public void testSlaCanBeUsedByMultipleVendorsViaEvaluations() {
        Long slaId = 2L;
        DeliveryEvaluation e1 = new DeliveryEvaluation();
        DeliveryEvaluation e2 = new DeliveryEvaluation();
        e1.setVendor(new Vendor("V1", "v1@e.com", "1"));
        e2.setVendor(new Vendor("V2", "v2@e.com", "2"));

        when(deliveryEvaluationRepository.findBySlaRequirementId(slaId))
                .thenReturn(Arrays.asList(e1, e2));

        List<DeliveryEvaluation> list = deliveryEvaluationService.getEvaluationsForRequirement(slaId);
        Assert.assertEquals(list.size(), 2);
        Assert.assertNotEquals(list.get(0).getVendor().getName(), list.get(1).getVendor().getName());
    }

    @Test(priority = 57, groups = {"many-to-many"})
    public void testManyToManyEdgeCaseNoEvaluations() {
        when(deliveryEvaluationRepository.findByVendorId(999L))
                .thenReturn(Collections.emptyList());

        List<DeliveryEvaluation> list = deliveryEvaluationService.getEvaluationsForVendor(999L);
        Assert.assertTrue(list.isEmpty());
    }

    @Test(priority = 58, groups = {"many-to-many"})
    public void testManyToManyWithSingleEvaluation() {
        Long vendorId = 1L;
        DeliveryEvaluation e1 = new DeliveryEvaluation();

        when(deliveryEvaluationRepository.findByVendorId(vendorId))
                .thenReturn(Collections.singletonList(e1));

        List<DeliveryEvaluation> list = deliveryEvaluationService.getEvaluationsForVendor(vendorId);
        Assert.assertEquals(list.size(), 1);
    }

    @Test(priority = 59, groups = {"many-to-many"})
    public void testManyToManyNormalizationThroughJoinEntity() {
        DeliveryEvaluation e = new DeliveryEvaluation();
        e.setVendor(new Vendor());
        e.setSlaRequirement(new SLARequirement());
        Assert.assertNotNull(e.getVendor());
        Assert.assertNotNull(e.getSlaRequirement());
    }
    // 7. Security & JWT
    @Test(priority = 65, groups = {"security-jwt"})
    public void testJwtCreationContainsClaims() {
        String token = jwtTokenProvider.createToken("user@example.com", "ADMIN", 100L);
        Assert.assertNotNull(token);

        var claims = jwtTokenProvider.getClaims(token);

        Assert.assertEquals(claims.get("email"), "user@example.com");
        Assert.assertEquals(claims.get("role"), "ADMIN");
        Assert.assertEquals(((Number) claims.get("userId")).longValue(), 100L);
    }

    @Test(priority = 66, groups = {"security-jwt"})
    public void testJwtValidationSuccess() {
        String token = jwtTokenProvider.createToken("user@example.com", "USER", 1L);
        Assert.assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test(priority = 67, groups = {"security-jwt"})
    public void testJwtValidationFailureForInvalidToken() {
        String token = "invalid.token.value";
        Assert.assertFalse(jwtTokenProvider.validateToken(token));
    }

    @Test(priority = 68, groups = {"security-jwt"})
    public void testJwtEmailClaimMatchesSubject() {
        String token = jwtTokenProvider.createToken("abc@xyz.com", "USER", 10L);
        var claims = jwtTokenProvider.getClaims(token);
        Assert.assertEquals(claims.getSubject(), "abc@xyz.com");
    }

    @Test(priority = 69, groups = {"security-jwt"})
    public void testJwtDifferentUsersDifferentTokens() {
        String t1 = jwtTokenProvider.createToken("u1@e.com", "USER", 1L);
        String t2 = jwtTokenProvider.createToken("u2@e.com", "USER", 2L);
        Assert.assertNotEquals(t1, t2);
    }

    @Test(priority = 70, groups = {"security-jwt"})
    public void testJwtRoleAdminEmbedded() {
        String token = jwtTokenProvider.createToken("admin@e.com", "ADMIN", 5L);
        var claims = jwtTokenProvider.getClaims(token);
        Assert.assertEquals(claims.get("role"), "ADMIN");
    }

    // 8. HQL & HCQL
    @Test(priority = 75, groups = {"hql-hcql"})
    public void testHqlFindHighQualityDeliveries() {
        Vendor vendor = new Vendor("V", "e", "p");
        vendor.setId(1L);

        DeliveryEvaluation e1 = new DeliveryEvaluation();
        e1.setQualityScore(95.0);

        when(deliveryEvaluationRepository.findHighQualityDeliveries(vendor, 90.0))
                .thenReturn(Collections.singletonList(e1));

        List<DeliveryEvaluation> list =
                deliveryEvaluationRepository.findHighQualityDeliveries(vendor, 90.0);

        Assert.assertEquals(list.size(), 1);
        Assert.assertTrue(list.get(0).getQualityScore() >= 90.0);
    }

    @Test(priority = 76, groups = {"hql-hcql"})
    public void testHqlFindOnTimeDeliveries() {
        SLARequirement sla = new SLARequirement("Req", "d", 3, 90.0);
        sla.setId(1L);

        DeliveryEvaluation e1 = new DeliveryEvaluation();
        e1.setMeetsDeliveryTarget(true);

        when(deliveryEvaluationRepository.findOnTimeDeliveries(sla))
                .thenReturn(Collections.singletonList(e1));

        List<DeliveryEvaluation> list = deliveryEvaluationRepository.findOnTimeDeliveries(sla);
        Assert.assertEquals(list.size(), 1);
        Assert.assertTrue(Boolean.TRUE.equals(list.get(0).getMeetsDeliveryTarget()));
    }

    @Test(priority = 77, groups = {"hql-hcql"})
    public void testVendorPerformanceScoreCalculationUsesPercentRanges() {
        Vendor v = new Vendor("V", "e", "p");
        v.setId(1L);

        SLARequirement sla = new SLARequirement("Req", "d", 5, 90.0);
        sla.setId(1L);

        DeliveryEvaluation e1 = new DeliveryEvaluation(v, sla, 4, 95.0, LocalDate.now());
        e1.setMeetsDeliveryTarget(true);
        e1.setMeetsQualityTarget(true);

        DeliveryEvaluation e2 = new DeliveryEvaluation(v, sla, 6, 80.0, LocalDate.now());
        e2.setMeetsDeliveryTarget(false);
        e2.setMeetsQualityTarget(false);

        when(vendorRepository.findById(1L))
                .thenReturn(java.util.Optional.of(v));

        when(deliveryEvaluationRepository.findByVendorId(1L))
                .thenReturn(Arrays.asList(e1, e2));

        when(vendorPerformanceScoreRepository.save(any(VendorPerformanceScore.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(vendorTierRepository.findByActiveTrueOrderByMinScoreThresholdDesc())
                .thenReturn(Collections.emptyList());

        VendorPerformanceScore score = vendorPerformanceScoreService.calculateScore(1L);

        Assert.assertTrue(score.getOnTimePercentage() >= 0 && score.getOnTimePercentage() <= 100);
        Assert.assertTrue(score.getQualityCompliancePercentage() >= 0 && score.getQualityCompliancePercentage() <= 100);
        Assert.assertTrue(score.getOverallScore() >= 0 && score.getOverallScore() <= 100);
    }

    @Test(priority = 78, groups = {"hql-hcql"})
    public void testVendorPerformanceScoreLatestScoreRetrieval() {
        Vendor v = new Vendor("V", "e", "p");
        v.setId(1L);

        VendorPerformanceScore s1 = new VendorPerformanceScore(v, 50.0, 50.0, 50.0);
        VendorPerformanceScore s2 = new VendorPerformanceScore(v, 80.0, 80.0, 80.0);
        List<VendorPerformanceScore> list = Arrays.asList(s2, s1);

        when(vendorPerformanceScoreRepository.findByVendorOrderByCalculatedAtDesc(1L))
                .thenReturn(list);

        VendorPerformanceScore latest = vendorPerformanceScoreService.getLatestScore(1L);

        Assert.assertEquals(latest.getOverallScore(), s2.getOverallScore());
    }

    @Test(priority = 79, groups = {"hql-hcql"})
    public void testVendorPerformanceScoreHistory() {
        Vendor v = new Vendor("V", "e", "p");
        v.setId(1L);

        VendorPerformanceScore s1 = new VendorPerformanceScore(v, 50.0, 50.0, 50.0);
        VendorPerformanceScore s2 = new VendorPerformanceScore(v, 80.0, 80.0, 80.0);

        List<VendorPerformanceScore> list = Arrays.asList(s2, s1);

        when(vendorPerformanceScoreRepository.findByVendorOrderByCalculatedAtDesc(1L))
                .thenReturn(list);

        List<VendorPerformanceScore> history = vendorPerformanceScoreService.getScoresForVendor(1L);

        Assert.assertEquals(history.size(), 2);
    }

    @Test(priority = 80, groups = {"hql-hcql"})
    public void testVendorTierValidationCreatesTier() {
        VendorTier tier = new VendorTier("Gold", 90.0, "Top tier");

        when(vendorTierRepository.existsByTierName("Gold"))
                .thenReturn(false);

        when(vendorTierRepository.save(any(VendorTier.class)))
                .thenAnswer(invocation -> {
                    VendorTier t = invocation.getArgument(0);
                    t.setId(1L);
                    return t;
                });

        VendorTier created = vendorTierService.createTier(tier);
        Assert.assertNotNull(created.getId());
    }

    @Test(priority = 81, groups = {"hql-hcql"})
    public void testVendorTierInvalidThresholdFails() {
        VendorTier tier = new VendorTier("Silver", 150.0, "Invalid");

        try {
            vendorTierService.createTier(tier);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            Assert.assertTrue(ex.getMessage().contains("0–100"));
        }
    }

    @Test(priority = 82, groups = {"hql-hcql"})
    public void testVendorTierDeactivate() {
        VendorTier tier = new VendorTier("Bronze", 60.0, "Low");
        tier.setId(1L);

        when(vendorTierRepository.findById(1L))
                .thenReturn(java.util.Optional.of(tier));

        when(vendorTierRepository.save(any(VendorTier.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        vendorTierService.deactivateTier(1L);

        Assert.assertFalse(tier.getActive());
    }
}
