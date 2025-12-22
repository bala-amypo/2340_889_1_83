public interface SLARequirementService {

    SLARequirement createRequirement(SLARequirement req);

    SLARequirement updateRequirement(Long id, SLARequirement req);

    SLARequirement getRequirementById(Long id);

    List<SLARequirement> getAllRequirements();

    void deactivateRequirement(Long id);
}
