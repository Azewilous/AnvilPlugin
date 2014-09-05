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
    FileSeperator fs = System.getProperty("file.separator");
    
    path = "plugins" + fs + "Anvil" + "License.txt";
    
    Path file = path;
    try {
        for(String s : license) {
          file.append(s);
        }
        Files.createFile(file);
    } catch (FileAlreadyExistsException x) {
        Bukkit.getServer().getLogger().info("License File - TRUE");
    } catch (IOException x) {
       x.printStackTrace();
    }
  }
  
  protected void removeLicense() {
    path = "plugins" + fs + "Anvil" + "License.txt";
        
  try {
       Files.delete(path);
    } catch (NoSuchFileException x) {
       Bukkit.getServer().getLogger().severe("License File does not exist!");
    } catch (DirectoryNotEmptyException x) {
       x.printStackTrace();
    } catch (IOException x) {
       x.printStackTrace();
    }
  }
      
}
