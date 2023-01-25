package Sprites;

import Screens.PlayScreen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;

import static com.mygdx.game.MiloshGame.PPM;

public class Ghos extends Sprite {
    public World world;
    private PlayScreen screen;
    public Body b2body;
    private TextureRegion ghosStand;
    private Texture playerSprite;


    public Ghos(World world){
    	  this.playerSprite = playerSprite;
          this.world = world;


          defineGhos();

    }


	public void defineGhos(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(64 / PPM,64 / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5 / PPM);

        fdef.shape = shape;
        b2body.createFixture(fdef);
    }


}
