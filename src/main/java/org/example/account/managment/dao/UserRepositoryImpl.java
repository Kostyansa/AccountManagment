package org.example.account.managment.dao;

import org.example.account.managment.entity.User;
import org.example.account.managment.exception.UserNotFoundException;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class UserRepositoryImpl implements UserRepository {

    private final Map<Long,User> users;

    private String path;

    /**
     * Creates User repository from folder with relative path:
     * {@link UserRepositoryImpl#path} from files with consecutive names 0,1...
     */
   public UserRepositoryImpl(String path) throws IOException, ClassNotFoundException {
       this.path = path;
       List<User> users = new LinkedList<>();
       File folder = new File(path);
       for (final File fileEntry : folder.listFiles()) {
           try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileEntry))) {
               users.add((User) inputStream.readObject());
           }
       }
       this.users = Collections.synchronizedMap(
               users.stream().collect(Collectors.toMap(User::getId, (User user) -> user))
       );
   }

   /**
    * Writes any cached in {@link UserRepositoryImpl#users} User objects
    * into folder with relative path: {@link UserRepositoryImpl#path}
    * where name of the file is user Id
    */
    public void saveCached(){
        List<User> users = this.get();
        users.stream().peek((User user) -> {
            try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(path + user.getId()))) {
                outputStream.writeObject(user);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new ServiceConfigurationError("File could not be written to", e);
            } catch (IOException e) {
                e.printStackTrace();
                throw new ServiceConfigurationError("IO Exception", e);
            }
        }).close();
    }

    @Override
    public List<User> get() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User get(Long id) throws UserNotFoundException {
        User user = users.get(id);
        if (user == null){
            throw new UserNotFoundException();
        }
        return users.get(id);
    }

    @Override
    public boolean update(User user) throws UserNotFoundException {
        if (users.replace(user.getId(), user) == null){
            throw new UserNotFoundException();
        }
        return true;
    }

    @Override
    public User save(User user) {
        if (user == null){
            throw new NullPointerException("User is null");
        }
        if (user.getId() == null){
            throw new NullPointerException("User id is null");

        }
        return users.put(user.getId(), user);
    }

    @Override
    public User delete(User user) {
        return users.remove(user.getId());
    }
}
