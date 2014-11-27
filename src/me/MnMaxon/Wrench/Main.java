package me.MnMaxon.Wrench;

import java.util.logging.Level;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
	public static Main plugin;
	public static Economy economy = null;

	@Override
	public void onEnable() {
		plugin = this;
		getServer().getPluginManager().registerEvents(new MainListener(), this);
		if (!setupEconomy())
			this.getServer().getLogger().log(Level.SEVERE, ChatColor.RED + "Wrench could not hook into vault");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You need to be a player to do this!");
			return false;
		}
		Player p = (Player) sender;
		if (args.length == 1 && args[0].equalsIgnoreCase("give"))
			if (p.hasPermission("Wrench.give"))
				if (p.getInventory().firstEmpty() != -1)
					p.getInventory().addItem(Main.getWrench());
				else
					p.sendMessage(ChatColor.DARK_RED + "Your inventory is full!");
			else
				p.sendMessage(ChatColor.DARK_RED + "You do not have permission to do this!");
		else
			displayHelp(sender);
		return false;
	}

	private void displayHelp(CommandSender s) {
		s.sendMessage(ChatColor.DARK_RED + "Use like /wrench give");
	}

	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(
				net.milkbowl.vault.economy.Economy.class);
		if (economyProvider != null) {
			economy = economyProvider.getProvider();
		}
		return (economy != null);
	}

	public static ItemStack getWrench() {
		ItemStack is = new ItemStack(Material.DIAMOND_PICKAXE);
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.RED + "Wrench");
		is.setItemMeta(im);
		return is;
	}
}