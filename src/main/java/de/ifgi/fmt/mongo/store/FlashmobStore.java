package de.ifgi.fmt.mongo.store;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import de.ifgi.fmt.model.Activity;
import de.ifgi.fmt.model.Comment;
import de.ifgi.fmt.model.Flashmob;
import de.ifgi.fmt.model.Role;
import de.ifgi.fmt.model.Role.Category;
import de.ifgi.fmt.model.User;
import de.ifgi.fmt.model.signal.VibrationSignal;
import de.ifgi.fmt.model.task.Task;
import de.ifgi.fmt.model.trigger.TimeTrigger;
import de.ifgi.fmt.model.trigger.Trigger;
import de.ifgi.fmt.utils.Utils;

public class FlashmobStore {
	
	
	public Flashmob getFlashmob(ObjectId id) {
		return DaoFactory.getFlashmobDao().get(id);
	}

	public void save(Flashmob f) {
		
		
		
		
		DaoFactory.getActivityDao().saveAll(f.getActivities());
		DaoFactory.getCommentDao().saveAll(f.getComments());
		DaoFactory.getRoleDao().saveAll(f.getRoles());
		DaoFactory.getFlashmobDao().save(f);
	}
	
	
	
	public static void main(String[] args) {
		GeometryFactory gf = new GeometryFactory();
		DateTime begin = new DateTime();
		
		Point p = gf.createPoint(new Coordinate(52.0, 7.0));
		
		User user1 = new User().setUsername("user1").setEmail("user1@fmt.de").setPassword("password1");
		User user2 = new User().setUsername("user2").setEmail("user2@fmt.de").setPassword("password2");
		User user3 = new User().setUsername("user3").setEmail("user3@fmt.de").setPassword("password3");;
		
		
		Role role1 = new Role()
			.setCategory(Category.EASY)
			.setDescription("Rolle 1")
			.setMaxCount(-1)
			.setMinCount(50)
			.setStartPoint(p)
			.setUsers(Utils.set(user1,user2));
		
		Role role2 = new Role()
			.setCategory(Category.EASY)
			.setDescription("Rolle 2")
			.setMaxCount(-1)
			.setMinCount(40)
			.setStartPoint(p)
			.setUsers(Utils.set(user3));
		
		Trigger t = new TimeTrigger()
			.setTime(begin.plusMinutes(5));
		
		Activity activity = new Activity()
			.setDescription("Activity 1")
			.setTrigger(t)
			.setSignal(new VibrationSignal())
			.addTask(role1, new Task().setDescription("Geh nach links"))
			.addTask(role2, new Task().setDescription("Geh nach rechts"));

		
		Flashmob f = new Flashmob()
			.addRole(role1)
			.addRole(role2)
			.addTrigger(t)
			.addComment(new Comment()
				.setText("war ganz dolle")
				.setUser(user1)
				.setTime(begin.plusHours(20)))
			.addActivity(activity)
			.setDescription("Was weiß ich")
			.setEnd(begin.plusHours(2))
			.setStart(begin)
			.setPublic(false)
			.setKey("geheim")
			.setTitle("Ein FlashMob")
			.setLocation(p);
		
		new UserStore().saveUsers(Utils.list(user1,user2,user3));
		new FlashmobStore().save(f);
		
		
	}
	
	
}
