package mchorse.bbs_mod.actions.types;

import mchorse.bbs_mod.actions.SuperFakePlayer;
import mchorse.bbs_mod.entity.ActorEntity;
import mchorse.bbs_mod.film.Film;
import mchorse.bbs_mod.film.replays.Replay;
import mchorse.bbs_mod.settings.values.ValueFloat;
import mchorse.bbs_mod.utils.clips.Clip;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class AttackActionClip extends ActionClip
{
    public final ValueFloat damage = new ValueFloat("damage", 0F);

    public AttackActionClip()
    {
        super();

        this.add(this.damage);
    }

    @Override
    public void applyAction(LivingEntity actor, SuperFakePlayer player, Film film, Replay replay, int tick)
    {
        float damageValue = this.damage.get();

        // Handle death sequence when damage is -1
        if (damageValue == -1F)
        {
            this.applyDeathSequence(actor, player, replay, tick);
            return;
        }

        if (damageValue <= 0F)
        {
            return;
        }

        this.applyPositionRotation(player, replay, tick);

        double distance = 6D;
        HitResult blockHit = player.raycast(distance, 1F, false);
        Vec3d origin = player.getCameraPosVec(1F);
        Vec3d rotation = player.getRotationVec(1F);
        Vec3d direction = origin.add(rotation.x * distance, rotation.y * distance, rotation.z * distance);

        double newDistance = blockHit != null ? blockHit.getPos().squaredDistanceTo(origin) : distance * distance;
        Box box = player.getBoundingBox().stretch(rotation.multiply(distance)).expand(1, 1, 1);
        EntityHitResult enittyHit = ProjectileUtil.raycast(actor == null ? player : actor, origin, direction, box, entity -> !entity.isSpectator() && entity.canHit(), newDistance);

        if (enittyHit != null)
        {
            Entity entity = enittyHit.getEntity();

            if (entity != null)
            {
                entity.damage(player.getWorld().getDamageSources().mobAttack(player), damageValue);
            }
        }
    }

    /**
     * Applies death sequence to the target entity when damage is -1
     * Includes hurt animation, poof particles, and entity removal
     */
    private void applyDeathSequence(LivingEntity actor, SuperFakePlayer player, Replay replay, int tick)
    {
        this.applyPositionRotation(player, replay, tick);

        double distance = 6D;
        HitResult blockHit = player.raycast(distance, 1F, false);
        Vec3d origin = player.getCameraPosVec(1F);
        Vec3d rotation = player.getRotationVec(1F);
        Vec3d direction = origin.add(rotation.x * distance, rotation.y * distance, rotation.z * distance);

        double newDistance = blockHit != null ? blockHit.getPos().squaredDistanceTo(origin) : distance * distance;
        Box box = player.getBoundingBox().stretch(rotation.multiply(distance)).expand(1, 1, 1);
        EntityHitResult entityHit = ProjectileUtil.raycast(actor == null ? player : actor, origin, direction, box, entity -> !entity.isSpectator() && entity.canHit(), newDistance);

        if (entityHit != null)
        {
            Entity targetEntity = entityHit.getEntity();

            if (targetEntity != null && targetEntity instanceof LivingEntity livingTarget)
            {
                // Apply hurt timer for death animation effect
                livingTarget.hurtTime = 10;
                
                // Spawn poof particles at entity location
                if (targetEntity.getWorld() instanceof ServerWorld serverWorld)
                {
                    Vec3d entityPos = targetEntity.getPos();
                    
                    // Spawn multiple poof particles in a small area around the entity
                    for (int i = 0; i < 5; i++)
                    {
                        double offsetX = (serverWorld.getRandom().nextDouble() - 0.5) * 0.5;
                        double offsetY = serverWorld.getRandom().nextDouble() * livingTarget.getHeight();
                        double offsetZ = (serverWorld.getRandom().nextDouble() - 0.5) * 0.5;
                        
                        serverWorld.spawnParticles(
                            ParticleTypes.POOF,
                            entityPos.x + offsetX,
                            entityPos.y + offsetY,
                            entityPos.z + offsetZ,
                            1,
                            0.0, 0.0, 0.0,
                            0.1
                        );
                    }
                }
                
                // Schedule entity removal
                if (targetEntity instanceof ActorEntity actorEntity)
                {
                    // For BBS actors, use discard method
                    actorEntity.discard();
                }
                else
                {
                    // For other living entities, remove with killed reason
                    targetEntity.remove(Entity.RemovalReason.KILLED);
                }
            }
        }
    }

    @Override
    protected Clip create()
    {
        return new AttackActionClip();
    }
}