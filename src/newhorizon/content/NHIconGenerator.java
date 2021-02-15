package newhorizon.content;

import arc.Core;
import arc.Settings;
import arc.graphics.Pixmap;
import arc.graphics.g2d.PixmapRegion;
import arc.graphics.g2d.TextureAtlas;
import arc.graphics.g2d.TextureRegion;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Log;
import mindustry.ctype.ContentType;
import mindustry.ctype.UnlockableContent;
import mindustry.game.Team;
import mindustry.gen.Icon;
import mindustry.graphics.MultiPacker;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
import mindustry.ui.Cicon;
import mindustry.ui.Styles;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatValue;
import newhorizon.NewHorizon;
import newhorizon.func.DrawFuncs;
import newhorizon.func.NHSetting;
import newhorizon.func.SettingDialog;

import static newhorizon.func.TableFuncs.LEN;

public class NHIconGenerator extends UnlockableContent{
	public NHIconGenerator(){
		super("specific-icon-generator");
		localizedName = Core.bundle.get("settings");
	}
	
	@Override
	public ContentType getContentType(){
		return ContentType.error;
	}
	
	@Override
	public TextureRegion icon(Cicon icon){
		return NHContent.ammoInfo;
	}
	
	@Override
	public void setStats(){
		stats.add(Stat.abilities, new StatValue(){
			@Override
			public void display(Table table){
				table.button("@settings", Icon.info, Styles.cleart, () -> new SettingDialog().show()).size(LEN * 3, LEN);
			}
		});
	}
	
	@Override
	public boolean isHidden(){
		return true;
	}
	
	@Override
	public void createIcons(MultiPacker packer){
		super.createIcons(packer);
		if(!NHSetting.getBool("@active.advance-load*"))return;
		NHLoader.fullIconNeeds.each( (name, iconSet) -> {
			TextureAtlas.AtlasRegion t = Core.atlas.find(iconSet.type.name);
			if(t.found()){
				PixmapRegion r = Core.atlas.getPixmap(t);
				Pixmap region = r.crop();
				
				Pixmap base = new Pixmap(r.width, r.height);
				base.drawPixmap(region);
				TextureAtlas.AtlasRegion cell = Core.atlas.find(iconSet.type.name + "-cell");
				if(cell != null && cell.found())base.drawPixmap(DrawFuncs.fillColor(Core.atlas.getPixmap(cell), Team.sharded.color), - 1, 0);
				
				for(Weapon w : iconSet.weapons){
					if(w.top)continue;
					DrawFuncs.drawWeaponPixmap(base, w, false);
				}
				
				base = DrawFuncs.getOutline(base, DrawFuncs.outlineColor);
				for(Weapon w : iconSet.weapons){
					if(!w.top)continue;
					DrawFuncs.drawWeaponPixmap(base, w, true);
				}
				packer.add(MultiPacker.PageType.main, name + "-icon", base);
			}else Log.info("[Create Fail]" + name);
		});
	}
	
	@Override
	public void load(){
		super.load();
		
		NHLoader.needBeLoad.each( (arg, tex) -> tex = Core.atlas.find(arg));
		//Vars.content.blocks().remove(NHLoader.iconGenerator);
	}
	
	public static class IconSet{
		public final Seq<Weapon> weapons = new Seq<>();
		public final UnitType type;
		
		public IconSet(UnitType type, Weapon[] weapons){
			this.type = type;
			this.weapons.addAll(weapons);
		}
	}
}