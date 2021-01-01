package game.game_objects;

import game.ImageResource;
import game.World;

public class Bullet extends GameObject {

    private final BulletDestructor destructor;
    private final double DEG2RAD = Math.PI / 180;
    private final float g = -9.8f;
    private final double angel;
    private final float initialX;
    private final float initialY;
    private final float initialVelocity;
    private float time;
    private final float[] mesh;
    private Tank source;
    private final ImageResource[] animations;
    boolean canMove = false;
    public Bullet(float angel, float initialX, float initialY, float initialVelocity, Tank destructor, Tank tank) {
        this.angel = -angel * DEG2RAD;
        this.initialX = initialX;
        this.initialY = initialY;
        this.initialVelocity = initialVelocity;
        this.time = 0;
        this.destructor = destructor;
        this.setWidth(1f);
        this.setHeight(1f);
        mesh = Ground.getInstance().getMesh();
        setImageResource(new ImageResource("/bullet.png"));
        this.source = tank;
        

        animations = new ImageResource[16];
        for (int i = 0; i < 16; i++) {
            animations[i] = new ImageResource("/" + (i + 1) + ".png");
        }

    }

    private void shoot() {

        //float deltaTime = GameDisplay.getInstance().getDeltaTime();
        time += .018;

        float velX = (float) (initialVelocity * Math.cos(angel));
        float velY = (float) (initialVelocity * Math.sin(angel));

        float currVelo = velY + g * time;

        float x = velX * time + initialX;
        float y = (float) (currVelo * time + initialY + .5 * g * Math.pow(time, 2));

        setX(x);
        setY(y);

        draw();

        if (getX() < 0 || getX() > 100) {
            World.getInstance().setShouldPlayNow(true);
            destructor.destroy();
            return;
        }

        if (getY() <= mesh[(int) getX()]) {
            World.getInstance().setShouldPlayNow(true);
            Ground.getInstance().destroyGround(getX());
            destructor.checkDamage(getX());
            World.getInstance().startBulletAnimation(getX(), getY(), animations);
            destructor.destroy();
        }
    }

    public void update() {
        shoot();
    }
}
