package Screens;

import Sprites.Ghos;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.MiloshGame;
import Sprites.Ghos;

import static com.mygdx.game.MiloshGame.PPM;

public class PlayScreen implements Screen {
    private MiloshGame game;
    Texture texture;
    private OrthographicCamera gamecam;
    private FitViewport gamePort;
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private World world;
    private Box2DDebugRenderer b2dr;
    private Ghos player;
    private boolean jump;
    private TextureAtlas atlas;
    private Texture playerSprite;
    private SpriteBatch batch;

    public PlayScreen(Game game){
        playerSprite = new Texture("Ghos1.png");
        atlas = new TextureAtlas("Ghos_Enemies.pack");
        this.game = (MiloshGame) game;
        jump = false;
        gamecam = new OrthographicCamera();
        gamePort = new FitViewport(MiloshGame.V_WIDTH / PPM, MiloshGame.V_HEIGHT / PPM,gamecam);
        maploader = new TmxMapLoader();
        map = maploader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);

        // sets the game's camera to be centered correctly
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2 , 0);

        world = new World(new Vector2(0,-200), true);
        b2dr = new Box2DDebugRenderer();

        player = new Ghos(world);

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        for(MapObject object : map.getLayers().get(1).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX()+ rect.getWidth() / 2) / PPM, (rect.getY() + rect.getHeight() / 2) / PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / PPM, rect.getHeight() / 2 / PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }


    }
    public TextureAtlas getAtlas(){
        return atlas;
    }
    public void update(float dt){
        handleInput(dt);
        
        world.step(1/60f, 6, 2);
        
        gamecam.update();
        renderer.setView(gamecam);

        // centers camera on player

        Vector3 position = gamecam.position;

        position.x = player.b2body.getPosition().x ;


        gamecam.position.set(position);
        gamecam.update();

    }
    public void handleInput(float dt){
        int horizontalForce = 0;

        if (player.b2body.getLinearVelocity().y == 0){
            jump = false;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            horizontalForce += 10;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            horizontalForce -= 10;
        
        }

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && !jump){
            player.b2body.applyForceToCenter(0,10000,false);
            jump = true;
        }
        player.b2body.setLinearVelocity(horizontalForce * 10, player.b2body.getLinearVelocity().y);




    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        renderer.setView(gamecam);
        renderer.render();


        game.batch.setProjectionMatrix(gamecam.combined);
        
        
       


        b2dr.render(world, gamecam.combined);
        
        game.batch.begin();
        game.batch.draw(playerSprite, player.b2body.getPosition().x - 5, player.b2body.getPosition().y - 5, 10, 10 );
        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    	game.batch.dispose();
    	b2dr.dispose();
    	world.dispose();

    }
}
