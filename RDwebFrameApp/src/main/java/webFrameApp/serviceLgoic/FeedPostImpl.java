package webFrameApp.serviceLgoic;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import webFrameApp.entites.FeedComment;
import webFrameApp.entites.FeedPost;
import webFrameApp.interfaces.FeedPostDAO;
import webFrameApp.repositories.FeedPostRepo;

//@Transactional
@Service
public class FeedPostImpl implements FeedPostDAO {
	@Autowired
	FeedPostRepo repo;
	@Autowired
	private static SessionFactory factory;

	@Override
	public void createPost(FeedPost post) {
		repo.save(post);
	}

	@Transactional
	@Override
	public List<FeedPost> listPosts() {
		List<FeedPost> listPost = repo.findAll();
		return listPost;

	}

	@Transactional
	@Override
	public FeedPost findById(int id) {

		System.out.println("get data");
		FeedPost p = repo.findOne(id);
		System.out.println(p.getComments().size() + " is the sizes");
		// session.close();
		return p;
	}

	@Transactional
	@Override
	public List<FeedComment> getComments(FeedPost post) {

		return post.getComments();
	}

}
