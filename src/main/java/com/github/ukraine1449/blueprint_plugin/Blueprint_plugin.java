package com.github.ukraine1449.blueprint_plugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

public final class Blueprint_plugin extends JavaPlugin {
public HashMap<Player, Integer> playerTierList = new HashMap<>();
public ArrayList<Material> tier1 = new ArrayList<>();
public ArrayList<Material> tier2 = new ArrayList<>();
public ArrayList<Material> tier3 = new ArrayList<>();
    @Override
    public void onEnable() {
        // Plugin startup logic
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        createCraftDB();
        createCustomRecipe();
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
    public void createCustomRecipe(){
        ItemStack totem = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta meta = totem.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Crafted totem");
        totem.setItemMeta(meta);
        NamespacedKey key = new NamespacedKey(this, "Crafted-Totem");
        ShapedRecipe recipe = new ShapedRecipe(key, totem);
        recipe.shape(" E ", "GGG", " G ");
        recipe.setIngredient('E', Material.EMERALD);
        recipe.setIngredient('G', Material.GOLD_INGOT);
        Bukkit.addRecipe(recipe);

        ItemStack saddle = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta smeta = saddle.getItemMeta();
        smeta.setDisplayName(ChatColor.GREEN + "Crafted Saddle");
        saddle.setItemMeta(smeta);
        NamespacedKey skey = new NamespacedKey(this, "Crafted-Saddle");
        ShapedRecipe srecipe = new ShapedRecipe(skey, saddle);
        srecipe.shape("LLL", "  S", "   ");
        srecipe.setIngredient('L', Material.LEATHER);
        srecipe.setIngredient('S', Material.STRING);
        Bukkit.addRecipe(srecipe);
    }
    public void addToList(Player player){
        int tier = getUserTier(player.getUniqueId().toString());
        playerTierList.put(player, tier);
    }
    public void addToTierLists(){
        tier1.add(Material.IRON_SWORD);
        tier1.add(Material.IRON_HELMET);
        tier1.add(Material.IRON_CHESTPLATE);
        tier1.add(Material.IRON_LEGGINGS);
        tier1.add(Material.IRON_BOOTS);
        tier1.add(Material.SHIELD);
        
        tier1.add(Material.DIAMOND_SWORD);
        tier1.add(Material.IRON_HELMET);
        tier1.add(Material.IRON_CHESTPLATE);
        tier1.add(Material.IRON_LEGGINGS);
        tier1.add(Material.IRON_BOOTS);
        tier1.add(Material.SHIELD);
    }
}
