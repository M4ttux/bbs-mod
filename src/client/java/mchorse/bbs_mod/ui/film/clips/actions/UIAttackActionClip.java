package mchorse.bbs_mod.ui.film.clips.actions;

import mchorse.bbs_mod.actions.types.AttackActionClip;
import mchorse.bbs_mod.ui.UIKeys;
import mchorse.bbs_mod.ui.film.IUIClipsDelegate;
import mchorse.bbs_mod.ui.framework.elements.input.UITrackpad;
import mchorse.bbs_mod.ui.utils.UI;

public class UIAttackActionClip extends UIActionClip<AttackActionClip>
{
    public UITrackpad damage;

    public UIAttackActionClip(AttackActionClip clip, IUIClipsDelegate editor)
    {
        super(clip, editor);
    }

    @Override
    protected void registerUI()
    {
        super.registerUI();

        this.damage = new UITrackpad((v) -> this.editor.editMultiple(this.clip.damage, (damageField) -> damageField.set(v.floatValue())));
        this.damage.tooltip(UIKeys.ACTIONS_ATTACK_DAMAGE_TOOLTIP);
    }

    @Override
    protected void registerPanels()
    {
        super.registerPanels();

        this.panels.add(UI.label(UIKeys.ACTIONS_ATTACK_DAMAGE).marginTop(12), this.damage);
    }

    @Override
    public void fillData()
    {
        super.fillData();

        this.damage.setValue(this.clip.damage.get());
    }
}