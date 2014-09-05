import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class LicenseHandler {
  
  private String version, name, path;
  private Directory directory;
  private ArrayList license;
  private boolean tampered, append_to_file;
  
 
  
  public String getLicense() {
    return license;
  }
  
  public String getLicenseName() {
    return name;
  }
  
  public String getLicenseVersion() {
    return version;
  }
  
  public boolean isTampered() {
    return (tamperTest() != null);
  }
  
  public Directory getFileLocation() {
    return this.directory;
  }



  

  protected boolean tamperTest() {
    
  }
  
  protected void createLicense() {
    
  }
  
}
