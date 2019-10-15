package commands.admin;

import commands.Command;
import main.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

import static main.Globals.logger;

/**
 * @author Patrick Ubelhor
 * @version 10/15/2019
 */
public class WhoIs extends Command {
	
	// Format string for each users' information
	private static String format =
			"```json" +
			"   User: %s#%s\n" +
			"     ID: %s\n" +
			"   Name: %s\n" +
			"Created: %s\n" +
			" Joined: %s\n" +
			" Status: %s\n" +
			" Avatar: %s\n" +
			"  Roles: %s```\n";
	
	
	public WhoIs(Permission perm) {
		super("whois", perm);
	}


	@Override
	public void run(MessageReceivedEvent event, String[] args) {
		TextChannel channel = event.getMessage().getTextChannel();
		List<Member> members = event.getMessage().getMentionedMembers();
		
		// Make sure the user entered at least one @mention
		if (members.isEmpty()) {
			logger.debug("Did not find any @mentions in message");
			channel.sendMessage("You must @mention 1 or more users/roles to identify!").queue();
			return;
		}
		
		// Send a message of information for each user requested
		for (Member member : members) {
			User user = member.getUser();
			
			String msg = String.format(format,
					user.getName(), user.getDiscriminator(),
					user.getId(),
					member.getEffectiveName(),
					user.getTimeCreated().toString(),
					member.getTimeJoined().toString(),
					member.getOnlineStatus().getKey(),
					user.getEffectiveAvatarUrl(),
					rolesToString(member.getRoles())
			);
			
			channel.sendMessage(msg).queue();
		}
		
	}
	
	
	/**
	 * Converts a list of roles into a comma-space separated string.
	 *
	 * @param roles A list of roles.
	 * @return A string representing the list of roles.
	 */
	private String rolesToString(List<Role> roles) {
		StringBuilder sb = new StringBuilder();
		
		for (Role role : roles) {
			sb.append(role.getName());
			sb.append(", ");
		}
		sb.delete(sb.length() - 2, sb.length());
		
		return sb.toString();
	}
	

	@Override
	public String getUsage() {
		return "whois @users";
	}
	

	@Override
	public String getDescription() {
		return "Gives information about the target users";
	}
}
