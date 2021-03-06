package org.jboss.xavier.integrations.route;

import org.apache.camel.EndpointInject;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.MockEndpointsAndSkip;
import org.apache.commons.io.IOUtils;
import org.jboss.xavier.analytics.pojo.input.workload.inventory.VMWorkloadInventoryModel;
import org.jboss.xavier.analytics.pojo.output.AnalysisModel;
import org.jboss.xavier.integrations.jpa.service.AnalysisService;
import org.junit.Test;

import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;


@MockEndpointsAndSkip("direct:vm-workload-inventory")
public class MainRouteBuilder_DirectCalculateVMWorkloadInventoryTest extends XavierCamelTest {
    @Inject
    AnalysisService analysisService;

    @EndpointInject(uri = "mock:direct:vm-workload-inventory")
    private MockEndpoint mockVmWorkloadInventory;

    @Test
    public void mainRouteBuilder_DirectCalculate_JSONGiven_ShouldReturnExpectedCalculatedValues() throws Exception {
        //Given
        AnalysisModel analysisModel = analysisService.buildAndSave("report name", "report desc", "file name", "user name", "user_account_number");

        String customerId = "CID123";
        String fileName = "cloudforms-export-v1.json";
        Long analysisId = analysisModel.getId();

        VMWorkloadInventoryModel expectedModel = new VMWorkloadInventoryModel();
        expectedModel.setVmName("dev-windows-server-2008-TEST");
        expectedModel.setProvider("VMware");
        expectedModel.setOsProductName("ServerNT");
        expectedModel.setNicsCount(1);
        expectedModel.setMemory(4294967296L);
        expectedModel.setHasRdmDisk(false);
        expectedModel.setGuestOSFullName("Microsoft Windows Server 2008 R2 (64-bit)");
        expectedModel.setDiskSpace(7437787136L);
        expectedModel.setDatacenter("V2V-DC");
        expectedModel.setCpuCores(1);
        expectedModel.setCluster("V2V_Cluster");
        expectedModel.setSystemServicesNames(Arrays.asList("{02B0078E-2148-45DD-B7D3-7E37AAB3B31D}","xmlprov","wudfsvc"));
        expectedModel.setVmDiskFilenames(Collections.singletonList("[NFS_Datastore] dev-windows-server-2008/dev-windows-server-2008.vmdk"));
        expectedModel.setAnalysisId(analysisId);

        expectedModel.setHost_name("esx13.v2v.bos.redhat.com");
        expectedModel.setVersion("6.5");
        expectedModel.setProduct("VMware vCenter");

        HashMap<String, String> files = new HashMap<>();
        files.put("/root/.bash_profile","# .bash_profile\n\n# Get the aliases and functions\nif [ -f ~/.bashrc ]; then\n\t. ~/.bashrc\nfi\n\n# User specific environment and startup programs\n\nPATH=$PATH:$HOME/bin\nexport PATH\nexport JAVA_HOME=/usr/java/jdk1.5.0_07/bin/java\nexport WAS_HOME=/opt/IBM/WebSphere/AppServer\n");
        files.put("/opt/IBM", null);
        expectedModel.setFiles(files);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("filename", fileName);
        metadata.put("org_id", customerId);
        metadata.put(RouteBuilderExceptionHandler.ANALYSIS_ID, analysisId.toString());

        Map<String, Object> headers = new HashMap<>();
        headers.put(RouteBuilderExceptionHandler.MA_METADATA, metadata);

        //When
        camelContext.start();
        camelContext.startRoute("calculate-vmworkloadinventory");
        String body = IOUtils.resourceToString(fileName, StandardCharsets.UTF_8, this.getClass().getClassLoader());

        camelContext.createProducerTemplate().sendBodyAndHeaders("direct:calculate-vmworkloadinventory", body, headers);

        Thread.sleep(5000);

        //Then
        assertThat(mockVmWorkloadInventory.getExchanges().stream().map(e -> e.getIn().getBody(VMWorkloadInventoryModel.class))
                .filter(e -> e.getVmName().equalsIgnoreCase("dev-windows-server-2008-TEST"))
                .findFirst().get())
                .isEqualToIgnoringNullFields(expectedModel);
        assertThat(mockVmWorkloadInventory.getExchanges().size()).isEqualTo(21);

        camelContext.stop();
    }

    @Test
    public void mainRouteBuilder_DirectCalculate_JSONOnVersion1_0_0Given_ShouldReturnExpectedCalculatedValues() throws Exception {
        //Given
        AnalysisModel analysisModel = analysisService.buildAndSave("report name", "report desc", "file name", "user name", "user_account_number");

        String customerId = "CID123";
        String fileName = "cloudforms-export-v1_0_0.json";
        Long analysisId = analysisModel.getId();

        VMWorkloadInventoryModel expectedModel = new VMWorkloadInventoryModel();
        expectedModel.setVmName("oracle_db");
        expectedModel.setProvider("vSphere");
        expectedModel.setOsProductName("Linux");
        expectedModel.setNicsCount(1);
        expectedModel.setMemory(8589934592L);
        expectedModel.setHasRdmDisk(false);
        expectedModel.setGuestOSFullName("CentOS Linux release 7.6.1810 (Core) ");
        expectedModel.setDiskSpace(17980588032L);
        expectedModel.setDatacenter("JON TEST DC");
        expectedModel.setCpuCores(2);
        expectedModel.setCluster("VMCluster");
        expectedModel.setSystemServicesNames(Arrays.asList("NetworkManager-dispatcher","NetworkManager-wait-online","NetworkManager"));
        expectedModel.setVmDiskFilenames(Arrays.asList("[NFS-Storage] oracle_db_1/", "[NFS-Storage] oracle_db_1/oracle_db.vmdk", "[NFS-Storage] oracle_db_1/"));
        expectedModel.setAnalysisId(analysisId);
        expectedModel.setScanRunDate(new SimpleDateFormat("yyyy-M-dd'T'hh:mm:ss.S").parse("2019-09-18T14:52:45.871Z"));
        expectedModel.setHost_name("host-47");
        expectedModel.setVersion("6.7.2");
        expectedModel.setProduct("VMware vCenter");

        HashMap<String, String> files = new HashMap<>();
        files.put("/etc/GeoIP.conf","dummy content");
        files.put("/etc/asound.conf", null);
        files.put("/etc/autofs.conf", null);
        expectedModel.setFiles(files);

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("filename", fileName);
        metadata.put("org_id", customerId);
        metadata.put(RouteBuilderExceptionHandler.ANALYSIS_ID, analysisId.toString());

        Map<String, Object> headers = new HashMap<>();
        headers.put(RouteBuilderExceptionHandler.MA_METADATA, metadata);

        //When
        camelContext.start();
        camelContext.startRoute("calculate-vmworkloadinventory");
        String body = IOUtils.resourceToString(fileName, StandardCharsets.UTF_8, this.getClass().getClassLoader());

        camelContext.createProducerTemplate().sendBodyAndHeaders("direct:calculate-vmworkloadinventory", body, headers);

        Thread.sleep(5000);

        //Then
        assertThat(mockVmWorkloadInventory.getExchanges().stream().map(e -> e.getIn().getBody(VMWorkloadInventoryModel.class))
                .filter(e -> e.getVmName().equalsIgnoreCase("oracle_db"))
                .findFirst().get())
                .isEqualToIgnoringNullFields(expectedModel);
        assertThat(mockVmWorkloadInventory.getExchanges().size()).isEqualTo(8);

        camelContext.stop();
    }

}
