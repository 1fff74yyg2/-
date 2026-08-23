JURASSICRAFT 1.12.2 -> 1.20.1 FORGE MIGRATION GUIDE
====================================================
Target: Forge 1.20.1-47.4.23, Mojang official mappings, Java 17.
Rules: adapt code to 1.20.1 Forge API. Do NOT change game logic/behavior. Keep the same
class/field/method names unless the API requires renaming. Keep everything compiling.

----------------------------------------------------------------------
1. BUILD / MOD INFRASTRUCTURE (already done)
----------------------------------------------------------------------
- build.gradle uses ForgeGradle 6.0.x, Forge 1.20.1-47.4.23, official mappings, Java 17.
- META-INF/mods.toml replaces mcmod.info. @Mod annotation now is @Mod("jurassicraft")
  (only value(); name/version/dependencies moved to mods.toml).
- No @SidedProxy in 1.20.1: use DistExecutor or an explicit static proxy instance
  created with DistExecutor.unsafeRunForDist(...).
- Main class lifecycle: constructor runs on mod construction. Use
  FMLJavaModLoadingContext.get().getModEventBus() for mod-bus events
  (RegisterEvent, FMLCommonSetupEvent) and MinecraftForge.EVENT_BUS for forge events.
- Old FMLPreInitializationEvent.init() logic moves into the constructor or into
  FMLCommonSetupEvent / RegisterEvent handlers.
- FMLServerStartingEvent -> RegisterCommandsEvent (MinecraftForge.EVENT_BUS), or
  override in the mod instance via the constructor: event -> register command.

2. PACKAGE / CLASS RENAMES (apply mechanically)
----------------------------------------------------------------------
- net.minecraft.entity.Entity            -> net.minecraft.world.entity.Entity
- net.minecraft.entity.EntityLivingBase   -> net.minecraft.world.entity.LivingEntity
- net.minecraft.entity.EntityLiving       -> net.minecraft.world.entity.Mob
- net.minecraft.entity.EntityCreature     -> net.minecraft.world.entity.PathfinderMob
- net.minecraft.entity.EntityAgeable      -> net.minecraft.world.entity.AgeableMob
- net.minecraft.entity.EntityAnimal       -> net.minecraft.world.entity.animal.Animal
- net.minecraft.entity.player.EntityPlayer-> net.minecraft.world.entity.player.Player
- net.minecraft.entity.player.EntityPlayerMP -> net.minecraft.server.level.ServerPlayer
- net.minecraft.entity.EntityItem         -> net.minecraft.world.entity.item.ItemEntity
- net.minecraft.entity.passive.EntityCow  -> net.minecraft.world.entity.animal.Cow
- net.minecraft.entity.passive.EntityPig  -> net.minecraft.world.entity.animal.Pig
- net.minecraft.entity.passive.EntitySheep-> net.minecraft.world.entity.animal.Sheep
- net.minecraft.entity.passive.EntityHorse-> net.minecraft.world.entity.animal.horse.Horse
- net.minecraft.entity.passive.EntityChicken -> net.minecraft.world.entity.animal.Chicken
- net.minecraft.entity.ai.EntityAIBase    -> net.minecraft.world.entity.ai.goal.Goal
- net.minecraft.entity.ai.EntityAITasks   -> net.minecraft.world.entity.ai.goal.GoalSelector
- net.minecraft.entity.ai.EntityAIWander  -> net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal
- net.minecraft.entity.ai.EntityAISwimming-> net.minecraft.world.entity.ai.goal.FloatGoal
- net.minecraft.entity.ai.EntityAIAttackMelee -> net.minecraft.world.entity.ai.goal.MeleeAttackGoal
- net.minecraft.entity.ai.EntityAIWatchClosest -> net.minecraft.world.entity.ai.goal.LookAtPlayerGoal
- net.minecraft.entity.ai.EntityAILookIdle-> net.minecraft.world.entity.ai.goal.RandomLookAroundGoal
- net.minecraft.entity.ai.EntityAIHurtByTarget -> net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal
- net.minecraft.entity.ai.EntityAINearestAttackableTarget -> net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal
- net.minecraft.entity.ai.EntityAIFollowOwner -> net.minecraft.world.entity.ai.goal.FollowOwnerGoal
- net.minecraft.entity.ai.EntityAISit     -> net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal
- net.minecraft.entity.ai.EntityMoveHelper-> net.minecraft.world.entity.ai.control.MoveControl
- net.minecraft.pathfinding.PathNavigate  -> net.minecraft.world.entity.ai.navigation.PathNavigation
- net.minecraft.pathfinding.PathFinder    -> net.minecraft.world.entity.ai.navigation.PathFinder
- net.minecraft.pathfinding.Path         -> net.minecraft.world.level.pathfinder.Path
- net.minecraft.world.World              -> net.minecraft.world.level.Level
- net.minecraft.util.math.BlockPos       -> net.minecraft.core.BlockPos
- net.minecraft.util.math.MathHelper     -> net.minecraft.util.Mth
- net.minecraft.util.EnumFacing          -> net.minecraft.core.Direction
- net.minecraft.util.EnumHand            -> net.minecraft.world.InteractionHand
- net.minecraft.util.EnumParticleTypes   -> net.minecraft.core.particles.ParticleTypes
- net.minecraft.util.DamageSource        -> net.minecraft.world.damagesource.DamageSource
- net.minecraft.util.ResourceLocation    -> net.minecraft.resources.ResourceLocation
- net.minecraft.util.SoundEvent          -> net.minecraft.sounds.SoundEvent
- net.minecraft.util.text.TextComponentString    -> net.minecraft.network.chat.Component
- net.minecraft.util.text.ITextComponent         -> net.minecraft.network.chat.Component
- net.minecraft.util.text.TextFormatting         -> net.minecraft.ChatFormatting
- net.minecraft.client.resources.I18n    -> net.minecraft.client.resources.language.I18n
- net.minecraft.init.Items               -> net.minecraft.world.item.Items
- net.minecraft.init.Blocks              -> net.minecraft.world.level.block.Blocks
- net.minecraft.item.Item                -> net.minecraft.world.item.Item
- net.minecraft.item.ItemStack           -> net.minecraft.world.item.ItemStack
- net.minecraft.item.ItemBlock           -> net.minecraft.world.item.BlockItem
- net.minecraft.item.ItemSpawnEgg        -> net.minecraft.world.item.SpawnEggItem
- net.minecraft.item.ItemFood            -> net.minecraft.world.food.FoodProperties (via Item.Properties)
- net.minecraft.block.Block              -> net.minecraft.world.level.block.Block
- net.minecraft.block.material.Material  -> use BlockBehaviour.Properties (no Material class)
- net.minecraft.block.state.IBlockState  -> net.minecraft.world.level.block.state.BlockState
- net.minecraft.tileentity.TileEntity    -> net.minecraft.world.level.block.entity.BlockEntity
- net.minecraft.util.ITickable           -> net.minecraft.world.level.block.entity.TickableBlockEntity
- net.minecraft.inventory.ISidedInventory-> implement net.minecraft.world.Container (+ getSlotsForFace via SidedContainer? In 1.20.1 use IInventory-like: net.minecraft.world.Container)
- net.minecraft.inventory.Container      -> net.minecraft.world.inventory.AbstractContainerMenu
- net.minecraft.inventory.Slot           -> net.minecraft.world.inventory.Slot
- net.minecraft.entity.player.InventoryPlayer -> net.minecraft.world.entity.player.Inventory
- net.minecraft.nbt.NBTTagCompound       -> net.minecraft.nbt.CompoundTag
- net.minecraft.nbt.NBTTagList           -> net.minecraft.nbt.ListTag
- net.minecraft.nbt.NBTTagString         -> net.minecraft.nbt.StringTag
- net.minecraft.nbt.NBTTagInt            -> net.minecraft.nbt.IntTag
- net.minecraft.nbt.NBTTagFloat          -> net.minecraft.nbt.FloatTag
- net.minecraft.network.datasync.DataManager   -> net.minecraft.network.syncher.SynchedEntityData
- net.minecraft.network.datasync.DataParameter -> net.minecraft.network.syncher.EntityDataAccessor
- net.minecraft.network.datasync.DataSerializers-> net.minecraft.network.syncher.EntityDataSerializers
- net.minecraftforge.fml.relauncher.Side -> net.minecraftforge.api.distmarker.Dist
- net.minecraftforge.fml.relauncher.SideOnly -> net.minecraftforge.api.distmarker.OnlyIn
- net.minecraftforge.fml.common.Mod      -> net.minecraftforge.fml.common.Mod (same, value only)
- net.minecraftforge.fml.common.eventhandler.SubscribeEvent -> net.minecraftforge.eventbus.api.SubscribeEvent
- net.minecraftforge.fml.common.registry.GameRegistry -> removed; use DeferredRegister / RegisterEvent / BuiltInRegistries
- net.minecraftforge.fml.common.network.simpleimpl.* -> removed; use net.minecraftforge.network.simple.SimpleChannel
- net.minecraftforge.fml.common.network.NetworkRegistry -> net.minecraftforge.network.NetworkRegistry (newSimpleChannel)
- net.minecraftforge.event.RegistryEvent -> net.minecraftforge.registries.RegisterEvent
- net.minecraftforge.common.MinecraftForge -> same
- net.minecraftforge.fluids.* -> net.minecraftforge.fluids.FluidType / ForgeFlowingFluid (big change)
- net.minecraft.client.model.ModelBase   -> net.minecraft.client.model.EntityModel (models here use vendored llibrary compat instead)
- net.minecraft.client.model.ModelRenderer -> vendored net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer
- net.minecraft.client.renderer.entity.RenderLiving -> net.minecraft.client.renderer.entity.LivingEntityRenderer
- net.minecraft.client.renderer.entity.RenderManager -> net.minecraft.client.renderer.entity.EntityRendererProvider.Context
- net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer -> net.minecraft.client.renderer.blockentity.BlockEntityRenderer
- net.minecraft.client.renderer.tileentity.BlockEntityRendererProvider -> net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
- net.minecraft.client.gui.GuiScreen     -> net.minecraft.client.gui.screens.Screen
- net.minecraft.client.gui.GuiContainer  -> net.minecraft.client.gui.screens.inventory.AbstractContainerScreen (or Screen)
- net.minecraft.client.gui.GuiButton     -> net.minecraft.client.gui.components.Button
- net.minecraft.util.math.AxisAlignedBB  -> net.minecraft.world.phys.AABB
- net.minecraft.util.math.Vec3d          -> net.minecraft.world.phys.Vec3
- net.minecraft.util.math.RayTraceResult -> net.minecraft.world.phys.HitResult
- net.minecraft.util.EnumActionResult    -> net.minecraft.world.InteractionResult
- org.lwjgl.opengl.GL11                 -> REMOVED; use PoseStack + VertexConsumer + RenderType

3. BLOCK API (1.20.1)
----------------------------------------------------------------------
- Constructor: super(Material.ROCK) -> super(BlockBehaviour.Properties.of().strength(3.0F, 5.0F))
  - setHardness(h) / setResistance(r) -> .strength(h, r) in Properties
  - setSoundType(SoundType.STONE) -> .sound(SoundType.STONE)
  - setHarvestLevel(tool, level) -> .requiresCorrectToolForDrops() + needs tool tags
  - setLightLevel(f) -> .lightLevel(blockState -> value)
  - setCreativeTab(tab) -> .creativeModeTab? In 1.20.1 use CreativeModeTab in Item.Properties instead; for blocks set via item properties.
  - setTranslationKey(String) -> REMOVED; descriptionId comes from registry name automatically.
  - getUnlocalizedName() -> getDescriptionId()
- getDefaultState() -> defaultBlockState()
- getMetaFromState(IBlockState) -> REMOVED (no metadata); use BlockState properties if needed.
- getStateFromMeta(int) -> REMOVED.
- onBlockActivated(World, BlockPos, IBlockState, EntityPlayer, EnumHand, EnumFacing, float, float, float)
  -> use(BlockState, Level, BlockPos, Player, InteractionHand, BlockHitResult)
- getItemDropped(IBlockState, Random, int) -> REMOVED; use loot tables (JSON in data/jurassicraft/loot_tables/blocks/*.json) or override getDrops.
- getBlockHardness -> getDestroySpeed(BlockState, BlockGetter, BlockPos)
- world.getBlockState(pos).getBlock() -> same
- getRenderType -> RenderShape: getRenderShape(BlockState) -> RenderShape.MODEL
- Block.isOpaqueCube() -> isOpaqueCube(BlockState) / use RenderShape
- Block.getLightValue -> getLightEmission
- Block.getBoundingBox -> getShape(BlockState, BlockGetter, BlockPos, CollisionContext)
- setBlockUnbreakable -> Properties.of().strength(-1.0F, 3600000.0F)

4. ITEM API (1.20.1)
----------------------------------------------------------------------
- Constructor: Item() -> Item(new Item.Properties())
  - setMaxStackSize(int) -> .stacksTo(int) in Properties
  - setMaxDamage(int) -> .durability(int) in Properties
  - setCreativeTab(tab) -> .creativeModeTab(tab) in Properties (1.20.1: tab param type CreativeModeTab)
  - setUnlocalizedName -> REMOVED (descriptionId from registry name)
  - getUnlocalizedName() -> getDescriptionId()
- onItemRightClick(World, EntityPlayer, EnumHand) -> use(Level, Player, InteractionHand)
- onItemUse(EntityPlayer, World, BlockPos, EnumHand, EnumFacing, float, float, float) -> useOn(UseOnContext)
- addInformation(ItemStack, World, List<String>, ITooltipFlag) -> appendHoverText(ItemStack, Level, List<Component>, TooltipFlag)
- getContainerItem -> getCraftingRemainingItem()
- hasContainerItem -> hasCraftingRemainingItem()
- ItemStack.getItemDamage() -> getDamageValue()
- ItemStack.setItemDamage(int) -> setDamageValue(int)
- ItemStack.getMaxDamage() -> getMaxDamage()
- ItemStack.getMetadata() -> getDamageValue()
- Item.getItemFromBlock(block) -> block.asItem()
- Items/Blocks constants: same names mostly (Items.IRON_INGOT etc.)

5. BLOCK ENTITY API (1.20.1) - TileEntity -> BlockEntity
----------------------------------------------------------------------
- class TileXxx extends TileEntity -> extends BlockEntity
- constructor: TileXxx() -> TileXxx(BlockPos pos, BlockState state)  (and pass super(BlockEntityType, pos, state))
- registerTileEntity(Class, ResourceLocation) -> DeferredRegister<BlockEntityType<?>> + BlockEntityType.Builder.of(...)
- getPos() -> getBlockPos()
- getWorld() -> getLevel()
- isRemote check: level.isClientSide
- ITickable -> implement TickableBlockEntity.tick() { ... } (tick(Level, BlockPos, BlockState, BlockEntity))
- Packet getUpdatePacket() -> getUpdateTag / ClientboundBlockEntityDataPacket.create(this, ...)
- SPacketUpdateTileEntity -> ClientboundBlockEntityDataPacket
- onDataPacket(NetworkManager, SPacketUpdateTileEntity) -> 1.20.1: override onDataPacket? Use handleUpdateTag(CompoundTag) or override getUpdateTag + handleUpdateTag.
- ISidedInventory -> implement net.minecraft.world.Container interface methods (getContainerSize, isEmpty, getItem, removeItem, removeItemNoUpdate, setItem, setChanged, stillValid, clearContent) + optional getSlotsForFace via SidedContainer interface (net.minecraft.world.SimpleContainer or implement SidedContainer)
- openInventory/closeInventory -> startOpen/stopOpen (Container methods)
- markDirty() -> setChanged()

6. ENTITY API (1.20.1)
----------------------------------------------------------------------
- EntityLivingBase -> LivingEntity, EntityLiving -> Mob, EntityCreature -> PathfinderMob
- entity.world -> entity.level, entity.isRemote -> level.isClientSide
- ticksExisted -> tickCount
- entityInit() -> defineSynchedData()
- DataManager.createKey(clazz, DataSerializers.VARINT) -> SynchedEntityData.defineId(clazz, EntityDataSerializers.INT)
- getDataManager().register(key, value) -> getEntityData().define(key, value)
- getDataManager().get(key) -> getEntityData().get(key)
- setAttackTarget -> setTarget, getAttackTarget -> getTarget
- applyEntityAttributes() -> registerGoals/attributes: in 1.20.1 use
  static AttributeSupplier.Builder createAttributes() and register via
  EntityAttributeCreationEvent; inside entity use getAttribute(Attributes.XXX)
- SharedMonsterAttributes.MAX_HEALTH -> Attributes.MAX_HEALTH (net.minecraft.world.entity.ai.attributes.Attributes)
- getEntityAttribute(SharedMonsterAttributes.X).setBaseValue(v) -> getAttribute(Attributes.X).setBaseValue(v)
- getAttributeMap().registerAttribute(attr) -> getAttributes().registerAttribute? (mostly not needed in 1.20.1)
- processInteract(EntityPlayer, EnumHand) -> interact(Player, InteractionHand): InteractionResult
- writeEntityToNBT -> addAdditionalSaveData(CompoundTag)
- readEntityFromNBT -> readAdditionalSaveData(CompoundTag)
- onUpdate() -> tick()
- onLivingUpdate() -> aiStep()
- onDeathUpdate -> tickDeath()
- attackEntityFrom(DamageSource, float) -> hurt(DamageSource, float)
- attackEntityAsMob(Entity) -> doHurtTarget(Entity)
- setEntityInvulnerable -> isInvulnerable checks
- getEntityBoundingBox() -> getBoundingBox()
- getRidingEntity() -> getVehicle()
- getPassengers() -> getPassengers() (same)
- isBeingRidden -> isVehicle()
- getMountedYOffset() -> getPassengersRidingOffset()
- Entity.getEntityId() -> getId()
- world.getEntityByID -> level.getEntity
- getDistanceSq -> distanceToSqr
- getEntityWorld -> level
- getHeldItemMainhand -> getMainHandItem
- getHeldItemOffhand -> getOffhandItem
- getHeldItem(EnumHand) -> getItemInHand(InteractionHand)
- getEquipmentInSlot -> getItemBySlot
- setDropItemsWhenDead -> (removed)
- canDespawn -> removeWhenFarAway
- isEntityAlive -> isAlive
- playSound(SoundEvent, float, float) -> playSound(SoundEvent, float, float) (same)
- world.playSound(Entity, BlockPos, SoundEvent, SoundCategory, float, float) -> level.playSound(Player, BlockPos, SoundEvent, SoundSource, float, float)
- EntityPlayer -> Player; player.capabilities.isCreativeMode -> player.getAbilities().instabuild
- player.inventory.currentItem -> player.getInventory().selected
- player.inventory.setInventorySlotContents(i, stack) -> player.getInventory().setItem(i, stack)
- player.inventory.mainInventory -> player.getInventory().items
- player.sendMessage(ITextComponent) -> player.sendSystemMessage(Component)
- player.getName() -> player.getName().getString()
- player.getDisplayName() -> player.getDisplayName()
- player.getGameProfile() -> player.getGameProfile()
- player.getPosition() -> player.blockPosition()
- player.posX -> player.getX() (fields removed; use getters)
- entity.posX -> entity.getX(), posY -> getY(), posZ -> getZ()
- entity.motionX -> getDeltaMovement().x; setVelocity via setDeltaMovement
- entity.rotationYaw -> getYRot(), rotationPitch -> getXRot(), prevRotationYaw -> yRotO, renderYawOffset -> yBodyRot
- entity.rotationYawHead -> yHeadRot
- entity.isJumping -> isJumping (same field)
- entity.moveForward -> getSpeed? (field still exists as zza? use getSpeed()/zza)
- entity.width/height -> getDimensions (immutable per type; use EntityType.getDimensions or override)
- setSize(w, h) -> override getDimensions(Pose) or use EntityType.Builder sized(...)
- EntityRegistry.registerModEntity -> DeferredRegister<EntityType<?>> + EntityType.Builder.of(EntityType.EntityFactory, MobCategory).sized(...).build(name)
- IEntityAdditionalSpawnData: writeSpawnData/readSpawnData (same)
- IEntityOwnable -> net.minecraft.world.entity.OwnableEntity
- getOwnerId() -> getOwnerUUID() (OwnableEntity)
- getOwner() -> getOwner()
- ItemSpawnEgg: new ItemSpawnEggJurassiCraft -> use SpawnEggItem with EntityType

7. AI API (1.20.1) - EntityAIBase -> Goal
----------------------------------------------------------------------
- class Xxx extends EntityAIBase -> extends Goal
- setMutexBits(int) -> setFlags(EnumSet.of(Goal.Flag.X, ...))
- shouldExecute() -> canUse()
- shouldContinueExecuting() -> canContinueToUse()
- startExecuting() -> start()
- resetTask() -> stop()
- updateTask() -> tick()
- isInterruptible -> isInterruptable
- tasks.addTask(priority, ai) -> goalSelector.addGoal(priority, goal)
- targetTasks.addTask -> targetSelector.addGoal
- EntityAITasks removed; entity has goalSelector/targetSelector fields
- Mob.getNavigator() -> getNavigation()
- navigation.clearPath() -> getNavigation().stop()
- navigator.tryMoveToEntityLiving -> navigation.moveTo(Entity, speed) or moveTo(pos, speed)
- getCreatureAttribute -> getMobType

8. RENDERING (1.20.1)
----------------------------------------------------------------------
- RenderingRegistry.registerEntityRenderingHandler -> EntityRenderers.register(EntityType, EntityRendererProvider)
- ClientRegistry.bindTileEntitySpecialRenderer -> BlockEntityRenderers.register(BlockEntityType, BlockEntityRendererProvider)
- RenderLiving -> LivingEntityRenderer<T extends LivingEntity, M extends EntityModel<T>>
- RenderManager -> EntityRendererProvider.Context
- RenderLiving constructor (RenderManager, ModelBase, float) -> (Context, M model, float)
- getEntityTexture -> getTextureLocation(T): ResourceLocation
- preRenderCallback(entity, float) -> scale(T, PoseStack, float)
- GL11.glPushMatrix/glPopMatrix -> poseStack.pushPose()/popPose()
- GL11.glTranslatef(x,y,z) -> poseStack.translate(x,y,z)
- GL11.glRotatef(deg, x,y,z) -> poseStack.mulPose(Axis.XP/Y/YP/ZP.rotationDegrees(deg))
- GL11.glScalef -> poseStack.scale
- bindEntityTexture -> RenderType.entityCutout(texture) via MultiBufferSource
- ModelBase.render(entity, ...) -> model.setupAnim(entity, ...) then model.renderToBuffer(poseStack, buffer, light, overlay)
- TileEntitySpecialRenderer.render(TE, x, y, z, partialTicks, destroyStage, alpha) -> BlockEntityRenderer.render(TE, partialTicks, PoseStack, MultiBufferSource, light, overlay)
- Item rendering: MinecraftForgeClient.registerItemRenderer removed -> use IClientItemExtensions or just JSON models
- RenderItem / ItemRenderer big changes - avoid if possible

9. GUI / CONTAINER (1.20.1)
----------------------------------------------------------------------
- GuiHandler (IGuiHandler) -> REMOVED. Use DeferredRegister<MenuType<?>> + Screen registration:
  - MenuType with MenuType.MenuSupplier + IContainerFactory
  - player.openMenu(MenuProvider) on server; MenuProvider.getMenuProvider creates menu
  - Register screens: MenuScreens.register(menuType, ScreenConstructor)
- Container -> AbstractContainerMenu:
  - constructor(menuType, id)
  - addSlotToContainer -> addSlot
  - listeners field -> (removed; use broadcastChanges())
  - detectAndSendChanges -> broadcastChanges()
  - IContainerListener.sendWindowProperty -> sendWindowData? Use ContainerData or DataSlot
  - getInventory -> slots collection
  - canInteractWith(player) -> stillValid(Inventory)
  - onContainerClosed -> removed(player)
  - transferStackInSlot -> quickMoveStack
  - mergeItemStack -> moveItemStackTo
- GuiContainer -> extends Screen (non-inventory) or AbstractContainerScreen<Menu> (inventory)
  - xSize/ySize -> imageWidth/imageHeight
  - guiLeft/guiTop -> leftPos/topPos
  - drawGuiContainerBackgroundLayer -> renderBg(GuiGraphics, float, int, int)
  - drawGuiContainerForegroundLayer -> renderLabels(GuiGraphics, int, int)
  - drawScreen -> render(GuiGraphics, int, int, float)
  - drawTexturedModalRect(x, y, u, v, w, h) -> guiGraphics.blit(texture, x, y, u, v, w, h)
  - fontRenderer.drawString -> guiGraphics.drawString(font, text, x, y, color)
  - renderHoveredToolTip -> renderTooltip(GuiGraphics, int, int) (call in render)
  - initGui -> init(); buttonList -> addRenderableWidget
  - GuiButton -> Button.builder(Component, onPress).bounds(x, y, w, h).build()
  - mc.player.closeScreen() -> mc.player.closeContainer() (or onClose())
  - updateScreen -> tick() (Screen has tick())
  - keyTyped -> keyPressed
  - mc.displayGuiScreen(gui) -> mc.setScreen(gui)
- I18n.format(key) -> Component.translatable(key).getString() or just Component.translatable(key)
- fontRenderer.getStringWidth -> font.width

10. NETWORK (1.20.1)
----------------------------------------------------------------------
- SimpleNetworkWrapper removed. Use:
  - static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
        new ResourceLocation("jurassicraft", "main"), () -> "1", "1"::equals, "1"::equals);
  - CHANNEL.registerMessage(id, MessageClass.class, MessageClass::encode,
        buf -> decode(buf), MessageClass::handle);  // handler: (msg, ctx) -> ...
  - Message classes: void encode(FriendlyByteBuf), static T decode(FriendlyByteBuf),
    void handle(Supplier<NetworkEvent.Context>).
  - sendToAll -> CHANNEL.send(PacketDistributor.ALL.noArg(), msg)
  - sendToServer -> CHANNEL.sendToServer(msg)
  - MessageContext -> NetworkEvent.Context (net.minecraftforge.network.NetworkEvent$Context)
  - ByteBuf -> FriendlyByteBuf (net.minecraft.network.FriendlyByteBuf)
  - ByteBufUtils.readVarInt(buf, 5) -> buf.readVarInt()
- llibrary AbstractMessage: vendored compat class exists at
  net.ilexiconn.llibrary.server.network.AbstractMessage; use it with the new signatures.

11. WORLD GEN (1.20.1)
----------------------------------------------------------------------
- IWorldGenerator removed. Use DeferredRegister<ConfiguredFeature<?,?>> + PlacedFeature, or
  simplest: register a Feature and add to biomes via BiomeLoadingEvent
  (MinecraftForge.EVENT_BUS: event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES)
  .add(() -> placedFeature)).
- WorldGenMinable(blockstate, count) -> net.minecraft.world.level.levelgen.feature.OreFeature
  with OreConfiguration.target(OreConfiguration.target(...), ...)
- world.getBlockState -> level.getBlockState; world.setBlockState -> level.setBlock

12. SOUND (1.20.1)
----------------------------------------------------------------------
- SoundEvent: DeferredRegister<SoundEvent>; new SoundEvent(ResourceLocation) ->
  SoundEvent.createVariableRangeEvent(ResourceLocation) (1.20.1) or registry-deferred.
- SoundCategory -> SoundSource (net.minecraft.sounds.SoundSource)

13. FLUIDS (1.20.1) - big change
----------------------------------------------------------------------
- net.minecraftforge.fluids.Fluid/FluidRegistry removed. Use:
  - DeferredRegister<FluidType> + DeferredRegister<Fluid> (ForgeFlowingFluid)
  - FluidType.Properties.create()... 
  - ForgeFlowingFluid.Properties + BlockStateProperties.LEVEL
  - BlockCultivateFluid extends LiquidBlock (net.minecraft.world.level.block.LiquidBlock)
  - Simplest: if fluid mechanics are not essential, replace the fluid with a
    normal block + keep the name, or use a minimal ForgeFlowingFluid pair.
  - RenderHelper.getFluidDisplayLists -> REMOVED; render fluid with BlockRenderer or
    skip the fluid overlay rendering.

14. CREATIVE TABS (1.20.1)
----------------------------------------------------------------------
- CreativeTabs -> CreativeModeTab
- new CreativeTabs("name") { getTabIconItem() } ->
  CreativeModeTab.builder().title(Component.translatable("itemGroup.jurassicraft.items"))
  .icon(() -> new ItemStack(item)).build()
- setCreativeTab(tab) on Item/Block -> tab param in Item.Properties
- Register via DeferredRegister<CreativeModeTab> or static field.
- 1.19+: itemGroup.* lang keys used for tab titles.

15. RESOURCES (1.20.1)
----------------------------------------------------------------------
- lang: assets/jurassicraft/lang/en_US.lang -> en_us.json ({"key":"value",...});
  keys: item.jurassicraft.xxx / block.jurassicraft.xxx / entity.jurassicraft.xxx.name
  (1.13+: descriptionId includes modid: e.g. "item.jurassicraft.amber")
- recipes: data/jurassicraft/recipes/*.json (shaped/shapeless/smelting); code recipes
  (GameRegistry.addShapedRecipe) must become JSON recipe files.
- loot tables: data/jurassicraft/loot_tables/blocks/*.json for block drops.
- blockstates: assets/jurassicraft/blockstates/*.json (variants format still works)
- models: assets/jurassicraft/models/block + models/item (same format)
- sounds.json exists.

16. LLIBRARY VENDORED COMPAT (already in src/main/java/net/ilexiconn/llibrary)
----------------------------------------------------------------------
- net.ilexiconn.llibrary.client.model.tools.AdvancedModelBase
- net.ilexiconn.llibrary.client.model.tools.AdvancedModelRenderer
- net.ilexiconn.llibrary.server.network.AbstractMessage
- net.ilexiconn.llibrary.server.util.WebUtils
These keep the old API surface used by the mod (rotateAngleX/Y/Z, rotationPointX/Y/Z,
addBox, setRotationPoint, addChild, updateDefaultPose, resetToDefaultPose, walk, flap,
swing, bob, faceTarget, chainSwing/Wave/Flap, moveBox, render(float scale) no-op,
render(PoseStack, VertexConsumer, int, int) real draw). Models extending
MowzieModelBase/AdvancedModelBase keep working: setupAnim calls the legacy
render(entity, ...) hook; renderToBuffer walks boxList roots and draws each part.
- @SideOnly(Side.CLIENT) -> @OnlyIn(Dist.CLIENT); imports updated.

17. MODEL FILES (entity models, client/model/entity/*, client/model/block/*)
----------------------------------------------------------------------
- class ModelXxx extends MowzieModelBase (extends AdvancedModelBase): keep as-is
  except imports: net.minecraft.util.math.MathHelper -> net.minecraft.util.Mth,
  net.minecraft.entity.Entity -> net.minecraft.world.entity.Entity,
  @SideOnly(Side.CLIENT) -> @OnlyIn(Dist.CLIENT).
- Fields MowzieModelRenderer stay; addBox/setRotationPoint/addChild/setRotateAngle stay.
- render(Entity entity, float f, ...) override: keep the animation logic; the legacy
  part.render(f5) calls are now no-ops (drawing happens via renderToBuffer).
- setRotationAngles(float, float, float, float, float, float, Entity) stays.
- Models used by TESRs (client/model/block/*) will be drawn directly by the BER with
  PoseStack: call part.render(PoseStack, VertexConsumer, int, int) or provide a helper.

18. COMPILE LOOP
----------------------------------------------------------------------
- Use: $env:JAVA_HOME='C:\Program Files\Java\jdk-17'; $env:GRADLE_USER_HOME='<proj>\.gradle-home';
  <proj>\..\tools\gradle-8.1.1\bin\gradle.bat compileJava --no-daemon
- Fix errors in dependency order: llibrary compat -> main/registries -> blocks/items ->
  tile entities -> entities/AI -> client (models/renderers/gui).
