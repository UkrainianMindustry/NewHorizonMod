package newhorizon.content;

import arc.Core;
import arc.graphics.Color;
import arc.graphics.Pixmaps;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Interp;
import arc.math.Mathf;
import arc.util.Log;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.ctype.ContentList;
import mindustry.entities.Effect;
import mindustry.entities.effect.MultiEffect;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;
import mindustry.graphics.MultiPacker;
import mindustry.graphics.Pal;
import mindustry.type.StatusEffect;
import newhorizon.NewHorizon;
import newhorizon.util.feature.ScreenInterferencer;
import newhorizon.util.func.NHPixmap;

public class NHStatusEffects implements ContentList{
    public static StatusEffect ultFireBurn,
            staticVel, emp1, emp2, emp3, invincible, quantization, accel_3, scrambler, end, phased, weak, scannerDown;
    
    @Override
    public void load(){
        ultFireBurn = new NHStatusEffect("ult-fire-burn"){{
            damage = 3;
            
            color = textureColor = NHColor.lightSkyBack;
            speedMultiplier = 1.2f;
            effect = NHFx.ultFireBurn;
        }};
        
        scannerDown = new NHStatusEffect("scanner-down"){{
            damage = 2;
            show = true;
            
            damageMultiplier = 0.95f;
            speedMultiplier = 0.9f;
            reloadMultiplier = 0.9f;
            
            effectChance = 0.2f;
            color = ScreenInterferencer.from.cpy().lerp(ScreenInterferencer.to, 0.5f);
            effect = new MultiEffect(NHFx.squareRand(ScreenInterferencer.from, 8f, 16f), NHFx.squareRand(ScreenInterferencer.to, 8f, 16f));
        }
    
            @Override
            public void update(Unit unit, float time){
               super.update(unit, time);
            
                if(unit.isLocal()){
                    ScreenInterferencer.continueGenerate();
                }
            }
        };
        
        weak = new NHStatusEffect("weak"){{
            damage = 10;
            speedMultiplier = 0.75f;
            damageMultiplier = 0.9f;
            reloadMultiplier = 0.9f;
            
            
            textureColor = color = NHColor.thurmixRed;
    
            effectChance = 0.25f;
            effect = new MultiEffect(new Effect(30, e -> {
                Draw.color(color);
                float drawSize = 24f * e.fout();
                Draw.rect(NHContent.pointerRegion, e.x, e.y - e.rotation * 24f * e.finpow(), drawSize, drawSize, -180);
            }), NHFx.crossBlast(color, 30, 45));
        }};
        
        phased = new NHStatusEffect("phased"){{
            damage = -10f;
            speedMultiplier = 1.25f;
            damageMultiplier = 1.15f;
    
            textureColor = color = NHColor.lightSkyBack;
            
            effectChance = 0.25f;
            effect = NHFx.squareRand(color, 8f, 16f);
        }};
        
        end = new NHStatusEffect("end"){{
            damage = 60;
            textureColor = color = NHColor.darkEnrColor;
            
            damageMultiplier = 0.5f;
            reloadMultiplier = 0.5f;
            speedMultiplier = 0.5f;
            
            effectChance = 0.1f;
            effect = new Effect(20f, 20f, e -> {
                Draw.color(Color.white, color, e.fin() + 0.35f);
                Lines.stroke(1.5f * e.fout(Interp.pow3Out));
                Lines.square(e.x, e.y, Mathf.randomSeed(e.id, 2f, 8f) * e.fin(Interp.pow2Out) + 6f, 45);
            });
        }
    
            @Override
            public void update(Unit unit, float time){
                if(!Vars.headless && Mathf.chanceDelta(0.1)){
                    Tmp.v1.rnd(Mathf.random(unit.hitSize() / 3.5f, unit.hitSize()) * 2f);
                    NHFx.shuttleLerp.at(unit.x + Tmp.v1.x, unit.y + Tmp.v1.y, Tmp.v1.angle(), color, Tmp.v1.len());
                }
            }
        };
        
        scrambler = new NHStatusEffect("scrambler-status"){{
            disarm = true;
            damage = 2;
            textureColor = color = NHColor.thermoPst;
            effectChance = 0.1f;
            effect = new MultiEffect(new Effect(30, e -> {
                Draw.color(color);
                float drawSize = 24f * e.fout();
                Draw.rect(NHContent.pointerRegion, e.x, e.y - e.rotation * 24f * e.finpow(), drawSize, drawSize, -180);
            }), NHFx.lightningHitSmall(color));
        }};
        
        accel_3 = new StatusEffect("accel_3"){{
            speedMultiplier = 3;
            show = false;
        }};
        
        quantization = new NHStatusEffect("quantization"){{
            textureColor = color = NHColor.darkEnrColor;
            effectChance = 0.1f;
            damage = -2f;
            effect = NHFx.squareRand(color, 5f, 13f);
            buildSpeedMultiplier = speedMultiplier = damageMultiplier = reloadMultiplier = 1.25f;
            healthMultiplier = 0.75f;
        }};
        
        invincible = new NHStatusEffect("invincible"){{
            healthMultiplier = 1000000;
        }
            @Override
            public void update(Unit unit, float time){
                if(!unit.spawnedByCore)unit.unapply(this);
            }
    
            @Override
            public void draw(Unit unit){
                if(!unit.spawnedByCore)return;
                Draw.z(Layer.effect);
                Draw.color(NHColor.lightSkyBack);
                TextureRegion area = Core.atlas.find(NewHorizon.name("upgrade"));
                Draw.rect(area, unit.x + unit.hitSize * 1.25f, unit.y, -90);
                Draw.rect(area, unit.x - unit.hitSize * 1.25f, unit.y, 90);
            }
        };
        
        staticVel = new NHStatusEffect("static-vel") {
            @Override
            public void update(Unit unit, float time){
                super.update(unit, time);
                unit.vel = unit.vel.scl(0.05f);
            }

            {
                permanent = true;
                this.color = Pal.gray;
                this.speedMultiplier = 0.00001F;
            }
        };
        
        emp1 = new NHStatusEffect("emp-1"){{
            damage = 0.05f;
            effect = NHFx.emped;
            effectChance = 0.1f;
            reactive = false;
            speedMultiplier = 0.8f;
            reloadMultiplier = 0.8f;
            damageMultiplier = 0.8f;
        }};
    
        emp2 = new NHStatusEffect("emp-2"){{
            damage = 0.15f;
            effect = NHFx.emped;
            effectChance = 0.2f;
            reactive = false;
            speedMultiplier = 0.6f;
            reloadMultiplier = 0.65f;
            damageMultiplier = 0.7f;
        }};
    
        emp3 = new NHStatusEffect("emp-3"){{
            damage = 0.25f;
            effect = NHFx.emped;
            effectChance = 0.3f;
            reactive = false;
            speedMultiplier = 0.4f;
            reloadMultiplier = 0.5f;
            damageMultiplier = 0.6f;
        }};
    }
	
	public static class NHStatusEffect extends StatusEffect{
		public Color textureColor = null;
		
		public NHStatusEffect(String name){
			super(name);
		}
        
        @Override
        public void load(){
            super.load();
        }
        
        @Override
		public void createIcons(MultiPacker packer){
		    TextureRegion region = Core.atlas.find(name);
		    
            Log.info(region);
		    
			if(NHPixmap.isDebugging() && region != null && region.found()){
				if(textureColor != null){
				    NHPixmap.addProcessed(name + "-full", NHPixmap.fillColor(Core.atlas.getPixmap(region), textureColor).outline(Color.valueOf("404049"), 3));
//					packer.add(MultiPacker.PageType.main, name + "-full", NHPixmap.fillColor(Core.atlas.getPixmap(fullIcon), textureColor).outline(Color.valueOf("404049"), 3));
				}else{
                    NHPixmap.addProcessed(name + "-full", Pixmaps.outline(Core.atlas.getPixmap(region), Color.valueOf("404049"), 3));
//                    packer.add(MultiPacker.PageType.main, name + "-full", Pixmaps.outline(Core.atlas.getPixmap(fullIcon), Color.valueOf("404049"), 3));
				}
			}else super.createIcons(packer);
		}
	}
}
