package com.codedex.soulmod.client;

import com.codedex.soulmod.SoulMod;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class MournerModel<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			ResourceLocation.fromNamespaceAndPath(SoulMod.MOD_ID, "mourner"), "main");

	private final ModelPart root;
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart rightItem;
	private final ModelPart right_arm;
	private final ModelPart left_arm;
	private final ModelPart left_wing;
	private final ModelPart right_wing;

	public MournerModel(ModelPart root) {
		this.root = root.getChild("root");
		this.head = this.root.getChild("head");
		this.body = this.root.getChild("body");
		this.rightItem = this.body.getChild("rightItem");
		this.right_arm = this.body.getChild("right_arm");
		this.left_arm = this.body.getChild("left_arm");
		this.left_wing = this.body.getChild("left_wing");
		this.right_wing = this.body.getChild("right_wing");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(-1, 0).addBox(-3.5F, -16.01F, -2.5F, 7.0F, 6.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 10).addBox(-1.5F, -10.0F, -1.0F, 3.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(0, 16).addBox(-1.5F, -10.0F, -1.0F, 3.0F, 8.0F, 2.0F, new CubeDeformation(-0.2F)), PartPose.offset(0.0F, -4.0F, 0.0F));

		PartDefinition rightItem = body.addOrReplaceChild("rightItem", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 5.0F, -2.0F, -1.3963F, 0.0F, 0.0F));

		PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(23, 0).addBox(-0.75F, -1.5F, -1.0F, 1.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.75F, -8.5F, 0.0F));

		PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(23, 6).addBox(-0.25F, -1.5F, -1.0F, 1.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.75F, -8.5F, 0.0F));

		PartDefinition left_wing = body.addOrReplaceChild("left_wing", CubeListBuilder.create(), PartPose.offset(1.5F, -8.0F, 1.0F));

		PartDefinition left_wing_r1 = left_wing.addOrReplaceChild("left_wing_r1", CubeListBuilder.create().texOffs(8, 6).addBox(0.3767F, -6.0F, -0.4088F, 0.0F, 10.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.5F, 0.0F, 0.0F, 0.0F, 0.6981F, 0.0F));

		PartDefinition right_wing = body.addOrReplaceChild("right_wing", CubeListBuilder.create(), PartPose.offset(-1.5F, -8.0F, 1.0F));

		PartDefinition right_wing_r1 = right_wing.addOrReplaceChild("right_wing_r1", CubeListBuilder.create().texOffs(8, 6).addBox(-0.3767F, -6.0F, -0.4088F, 0.0F, 10.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.5F, 0.0F, 0.0F, 0.0F, -0.6981F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		// Logique d'animation (à remplir plus tard)
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}
}