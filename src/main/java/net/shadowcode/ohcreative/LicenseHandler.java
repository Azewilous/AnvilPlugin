public class LicenseHandler {
  
  private String version, name;
  private Directory directory;
  private ArrayList license;
  private boolean tampered;
  
  public LicenseHandler() {
    this.tampered = null;
    this.version = "";
    this.name = "";
  }
  
  public boolean parseLicense() {
  
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
    return findFile();
  }



  

  protected boolean tamperTest() {
    
  }
  
  protected Directory findFile() {
    
    return this.directory;
  }
  
}
