public class LicenseHandler {
  
  private String version, name;
  private Directory directory;
  private ArrayList license;
  private boolean tampered;
  
  public void createLicense() {
    
  }
  
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
  
}
