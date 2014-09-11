public class LicenseHandler {
  
  private LicenseHandler handler;
  
  private String version, name, path;
  private Directory directory;
  private License license;

 
  public LicenseHandler getInstance() {
    return handler;
  }
  
  public ArrayList<String> getLicense() {
    return license;
  }
  
  public String getLicenseName() {
    return name;
  }
  
  public String getLicenseVersion() {
    return version;
  }
  
  public Directory getFileLocation() {
    return this.directory;
  }
  
  protected void createLicense() {
    FileSeperator fs = System.getProperty("file.separator");
    
    path = "plugins" + fs + "Anvil" + "License.txt";
    
    Path file = path;
    try {
        for(String s : license) {
          file.append(s);
        }
        Files.createFile(file);
    } catch (FileAlreadyExistsException x) {
        Bukkit.getServer().getLogger().info("License File exists!");
    } catch (IOException x) {
       x.printStackTrace();
    }
  }
      
}
