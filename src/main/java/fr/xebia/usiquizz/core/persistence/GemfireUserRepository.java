package fr.xebia.usiquizz.core.persistence;

import fr.xebia.usiquizz.core.persistence.serialization.UserSerializer;

public class GemfireUserRepository implements UserRepository {

    private GemfireRepository gemfireRepository;

    public GemfireUserRepository(GemfireRepository gemfireRepository) {
        this.gemfireRepository = gemfireRepository;
    }

    private UserSerializer userSerializer = new UserSerializer();

    @Override
    public void insertUser(String email, String password, String firstname, String lastname) throws UserAlreadyExists {
        gemfireRepository.getUserRegion().put(email, userSerializer.serializeUser(new User(email, password, firstname, lastname)));
    }

    @Override
    public User getUser(String mail) {
        return userSerializer.deserializeUser(gemfireRepository.getUserRegion().get(mail));
    }

    @Override
    public boolean checkUserWithEmailExist(String email) {
        return gemfireRepository.getUserRegion().get(email) != null;
    }

    @Override
    public User logUser(String mail, String password) {
        byte[] serUser = gemfireRepository.getUserRegion().get(mail);
        if(serUser==null){
            return null;
        }
        User user = userSerializer.deserializeUser(serUser);
        return (user != null && user.getPassword().equals(password) ? user : null);
    }
}
