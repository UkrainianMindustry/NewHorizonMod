package newhorizon.expand.bullets;

import mindustry.content.Fx;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Bullet;

public class EffectBulletType extends BulletType{
	public EffectBulletType(float lifetime){
		super();
		this.lifetime = lifetime;
		despawnEffect = hitEffect = shootEffect = smokeEffect = trailEffect = Fx.none;
		absorbable = collides = collidesAir = collidesGround = collidesTeam = collidesTiles = false;
		hitSize = 0;
		speed = 0.0001f;
		drawSize = 120f;
    }

	@Override public void draw(Bullet b){}
	@Override public void drawLight(Bullet b){}
}














