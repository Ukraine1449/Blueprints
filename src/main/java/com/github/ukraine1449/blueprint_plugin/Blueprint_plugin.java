package com.github.ukraine1449.blueprint_plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public final class Blueprint_plugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        createCraftDB();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public Connection getConnection() throws Exception{
        String ip = getConfig().getString("DB.ip");
        String password = getConfig().getString("DB.password");
        String username = getConfig().getString("DB.username");
        String dbn = getConfig().getString("DB.database_name");//these 4 strings get the login info from config.yml file, and use that for DB connections
        try{
            String driver = "com.mysql.jdbc.Driver";
            String url = "jdbc:mysql://"+ ip + ":3306/" + dbn;
            System.out.println(url);
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("Connected");
            return conn;
        }catch(Exception e){
            System.out.println("Unable to connect to SQL server.");
            e.printStackTrace();
        }
        return null;
    }
    public void createCraftDB(){
        Bukkit.getScheduler().runTaskAsynchronously(getServer().getPluginManager().getPlugin("Resource-Minions"), new Runnable() {
            @Override
            public void run () {
                    try{
                        Connection con = getConnection();
                        PreparedStatement create = con.prepareStatement("CREATE TABLE IF NOT EXISTS userTiers(UUID varchar(255),tier int, PRIMARY KEY (UUID))");
                        create.executeUpdate();
                        con.close();
                    }catch(Exception e){e.printStackTrace();}
            }
        });
    }
    public void insertFirstTime(String UUID){
        Bukkit.getScheduler().runTaskAsynchronously(getServer().getPluginManager().getPlugin("Resource-Minions"), new Runnable() {
            @Override
            public void run () {
                try{
                    Connection con = getConnection();
                    PreparedStatement posted = con.prepareStatement("INSERT INTO userTiers(tier) VALUES (0)ON DUPLICATE KEY UPDATE UUID='"+UUID+"'");
                    posted.executeUpdate();
                    con.close();
                }catch (Exception e){
                    e.printStackTrace();
                }            }
        });
    }
    public void updateUser(Integer tier, String UUID){
        Bukkit.getScheduler().runTaskAsynchronously(getServer().getPluginManager().getPlugin("Resource-Minions"), new Runnable() {
            @Override
            public void run () {
                try{
                    Connection con = getConnection();
                    PreparedStatement posted = con.prepareStatement("UPDATE userTiers SET tier="+tier+" WHERE UUID='"+UUID+"'");
                    posted.executeUpdate();
                    con.close();
                }catch (Exception e){
                    e.printStackTrace();
                }            }
        });
    }
    public Integer getUserTier(String UUID){
        final Integer[] tier = {null};
        Bukkit.getScheduler().runTaskAsynchronously(getServer().getPluginManager().getPlugin("Resource-Minions"), new Runnable() {
            @Override
            public void run () {
                try {
                    Connection con = getConnection();
                    PreparedStatement statement = con.prepareStatement("SELECT tier FROM userTiers WHERE ID='"+UUID+"'");
                    ResultSet result = statement.executeQuery();
                    while(result.next()){
                        tier[0] = result.getInt("tier");
                    }

                    con.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        return tier[0];
    }
}
