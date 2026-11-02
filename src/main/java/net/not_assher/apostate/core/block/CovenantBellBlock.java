package net.not_assher.apostate.core.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.not_assher.apostate.api.block.LightEmitter;
import net.not_assher.apostate.core.block.entity.CovenantBellBlockEntity;
import org.jspecify.annotations.Nullable;

/**
 * @author Chemthunder
 */
public class CovenantBellBlock extends BlockWithEntity implements LightEmitter {
    public static final BooleanProperty HAS_STACK = BooleanProperty.of("has_stack");
    public static final EnumProperty<Direction> DIR = HorizontalFacingBlock.FACING;

    public CovenantBellBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(HAS_STACK, false).with(DIR, Direction.NORTH));
    }

    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HAS_STACK);
        builder.add(DIR);
    }

    @Nullable
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(DIR, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return createCodec(CovenantBellBlock::new);
    }

    @Nullable
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new CovenantBellBlockEntity(pos, state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return ((world1, pos, state1, blockEntity) -> {
            if (blockEntity instanceof CovenantBellBlockEntity bell) {
                bell.tick(world1, pos, state1, bell);

                if (bell.getPactStack().isEmpty())
                    return;

                if (bell.getPactStack() != null) {
                    if (!world1.getBlockState(pos).get(HAS_STACK)) {
                        world1.setBlockState(pos, state1.with(HAS_STACK, true));
                    }
                } else {
                    if (world1.getBlockState(pos).get(HAS_STACK)) {
                        world1.setBlockState(pos, state1.with(HAS_STACK, false));
                    }
                }
            }
        });
    }

    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.union(Block.createColumnShape(6.0F, 6.0F, 13.0F), Block.createColumnShape(8.0F, 4.0F, 6.0F));
    }

    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.union(Block.createColumnShape(6.0F, 6.0F, 13.0F), Block.createColumnShape(8.0F, 4.0F, 6.0F));
    }

    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(DIR, rotation.rotate(state.get(DIR)));
    }

    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(DIR)));
    }

    public static boolean hasEmissiveLighting(BlockState state, BlockView world, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof CovenantBellBlockEntity entity) {
            return entity.getPactStack() != null;
        }
        return false;
    }

    public static int getLuminance(BlockState state) {
        return state.get(HAS_STACK) ? 10 : 0;
    }
}
