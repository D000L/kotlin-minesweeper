package minesweeper

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.maps.shouldContainKey
import io.kotest.matchers.shouldBe
import minesweeper.domain.Board
import minesweeper.domain.BoardSize
import minesweeper.domain.Cell
import minesweeper.domain.Coordinate
import minesweeper.domain.Coordinates
import minesweeper.domain.MineCount
import minesweeper.domain.MineSpawner

class BoardTest : DescribeSpec({

    describe("generate") {
        context("높이와 너비가 주어지면") {
            it("해당 크기의 보드를 생성한다.") {
                val board = Board.generate(BoardSize(2, 2), MineCount(1))
                board.map shouldContainKey Coordinate(0, 0)
                board.map shouldContainKey Coordinate(0, 1)
                board.map shouldContainKey Coordinate(1, 1)
                board.map shouldContainKey Coordinate(1, 1)
            }
        }

        context("크기보다 지뢰의 개수가 많으면") {
            it("Invalid 상태를 반환한다.") {
                shouldThrow<IllegalArgumentException> {
                    Board.generate(BoardSize(1, 1), MineCount(10))
                }
            }
        }

        context("지뢰의 개수가 만큼") {
            it("지뢰를 보드에 추가한다.") {
                val board = Board.generate(BoardSize(2, 2), MineCount(2))
                board.remainMineCount() shouldBe 2
            }
        }

        context("지정된 위치에") {
            it("지뢰를 배치한다.") {
                val mineSpawner = TestMineSpawner(listOf(Coordinate(0, 0), Coordinate(1, 1)))
                val board = Board.generate(BoardSize(2, 2), MineCount(2), mineSpawner)

                board.map[Coordinate(0, 0)] shouldBe Cell.Mine
                board.map[Coordinate(0, 1)] shouldBe Cell.None
                board.map[Coordinate(1, 0)] shouldBe Cell.None
                board.map[Coordinate(1, 1)] shouldBe Cell.Mine
            }
        }
    }
})

class TestMineSpawner(private val coordinates: List<Coordinate>) : MineSpawner {
    override fun spawn(boardSize: BoardSize, count: MineCount): Coordinates {
        return Coordinates(coordinates.toSet())
    }
}
