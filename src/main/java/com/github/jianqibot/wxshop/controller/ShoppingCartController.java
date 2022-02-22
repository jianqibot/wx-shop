package com.github.jianqibot.wxshop.controller;

import com.github.jianqibot.wxshop.entity.*;
import com.github.jianqibot.wxshop.service.ShoppingCartService;
import com.github.jianqibot.wxshop.service.UserContext;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @Inject
    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    /**
     * @api {get} /shoppingCart fetch list of goods in the shopping cart under current user
     * @apiName GetShoppingCart
     * @apiGroup shopping cart
     *
     * @apiHeader {String} Accept application/json
     *
     * @apiParam {Number} pageNum page number(starting from 1)
     * @apiParam {Number} pageSize page size
     *
     * @apiSuccess {Number} pageNum page number(starting from 1)
     * @apiSuccess {Number} pageSize page size
     * @apiSuccess {Number} totalPage page number in total
     * @apiSuccess {ShoppingCart} data goods in shopping cart, grouped by shop id
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *       "pageNum": 1,
     *       "pageSize": 10,
     *       "totalPage": 5,
     *       "data": [
     *         {
     *           "shop": {
     *              "id": 12345,
     *              "name": "my shop",
     *              "description": "my apple shop",
     *              "imgUrl": "https://img.url",
     *              "ownerUserId": 12345,
     *              "createdAt": "2020-03-22T13:22:03Z",
     *              "updatedAt": "2020-03-22T13:22:03Z"
     *            },
     *            "goods": [
     *              {
     *                  "id": 12345,
     *                  "name": "soap",
     *                  "description": "coconut soap",
     *                  "details": "coconut soap made with premium material",
     *                  "imgUrl": "https://img.url",
     *                  "price": 500,
     *                  "number": 10,
     *                  "createdAt": "2020-03-22T13:22:03Z",
     *                  "updatedAt": "2020-03-22T13:22:03Z"
     *              },
     *              {
     *                    ...
     *              }
     *           ]
     *         }
     *       ]
     *     }
     *
     * @apiError 401 Unauthorized if user hasn't login yet
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 401 Unauthorized
     *     {
     *       "message": "Unauthorized"
     *     }
     */
    /**
     * @param pageNum  page number
     * @param pageSize page size
     * @return page response of ShoppingCartResponse type
     */

    @GetMapping("/shoppingCart")
    @ResponseBody
    public PageResponse<ShoppingCartResponse> shopCart(@RequestParam("pageNum") Integer pageNum,
                                                       @RequestParam("pageSize") Integer pageSize) {
        return shoppingCartService.getShopCart(pageNum, pageSize, UserContext.getCurrentUser().getId());
    }


    /**
     * @api {post} /shoppingCart add shopping cart
     * @apiName AddShoppingCart
     * @apiGroup shopping cart
     * @apiHeader {String} Accept application/json
     * @apiHeader {String} Content-Type application/json
     * @apiParamExample {json} Request-Example:
     * {
     * "goods": [
     * {
     * "id": 12345,
     * "number": 10,
     * },
     * {
     * ...
     * }
     * }
     * @apiSuccess {ShoppingCart} data goods list of that shop after update
     * @apiSuccessExample Success-Response:
     * HTTP/1.1 200 OK
     * {
     * "data": {
     * "shop": {
     * "id": 12345,
     * "name": "my shop",
     * "description": "my soap shop",
     * "imgUrl": "https://img.url",
     * "ownerUserId": 12345,
     * "createdAt": "2020-03-22T13:22:03Z",
     * "updatedAt": "2020-03-22T13:22:03Z"
     * },
     * "goods": [
     * {
     * "id": 12345,
     * "name": "soap",
     * "description": "coconut soap",
     * "details": "coconut soap made with premium material",
     * "imgUrl": "https://img.url",
     * "address": "XXX",
     * "price": 500,
     * "number": 10,
     * "createdAt": "2020-03-22T13:22:03Z",
     * "updatedAt": "2020-03-22T13:22:03Z"
     * },
     * {
     * ...
     * }
     * ]
     * }
     * }
     * }
     * @apiError 400 Bad Request if request contains error
     * @apiError 401 Unauthorized if user has not login yet
     * @apiError 404 Not Found if goods cannot be found
     * @apiErrorExample Error-Response:
     * HTTP/1.1 401 Unauthorized
     * {
     * "message": "Unauthorized"
     * }
     */
    /**
     *
     * @param GoodsToBeAdded new goods to be added
     * @param response HttpServletResponse
     * @return response of ShoppingCartResponse type
     */

    @PostMapping("/shoppingCart")
    @ResponseBody
    public Response<ShoppingCartResponse> addToShoppingCart(@RequestBody GoodsInShoppingCart GoodsToBeAdded,
                                                   HttpServletResponse response) {
        try {
            return shoppingCartService.updateShoppingCart(GoodsToBeAdded, UserContext.getCurrentUser().getId());
        } catch (HttpException e) {
            response.setStatus(e.getStatusCode());
        }
        return Response.of(null);
    }


    /**
     * @api {delete} /shoppingCart/:goodsId 删除当前用户购物车中指定的商品
     * @apiName DeleteShoppingCart
     * @apiGroup shopping cart
     *
     * @apiHeader {String} Accept application/json
     *
     * @apiParam {Number} goodsId ID of the goods to be deleted
     *
     * @apiSuccess {ShoppingCart} data goods list of that shop after deletion
     *
     * @apiSuccessExample Success-Response:
     *     HTTP/1.1 200 OK
     *     {
     *       "data": {
     *           "shop": {
     *              "id": 12345,
     *              "name": "my shop",
     *              "description": "my soap shop",
     *              "imgUrl": "https://img.url",
     *              "ownerUserId": 12345,
     *              "createdAt": "2020-03-22T13:22:03Z",
     *              "updatedAt": "2020-03-22T13:22:03Z"
     *            },
     *            "goods": [
     *              {
     *                  "id": 12345,
     *                  "name": "soap",
     *                  "description": "coconut soap",
     *                  "details": "coconut soap made with premium material",
     *                  "imgUrl": "https://img.url",
     *                  "address": "XXX",
     *                  "price": 500,
     *                  "number": 10,
     *                  "createdAt": "2020-03-22T13:22:03Z",
     *                  "updatedAt": "2020-03-22T13:22:03Z"
     *              },
     *              {
     *                    ...
     *              }
     *           ]
     *         }
     *       }
     *     }
     *
     * @apiError 401 Unauthorized 若用户未登录
     *
     * @apiErrorExample Error-Response:
     *     HTTP/1.1 401 Unauthorized
     *     {
     *       "message": "Unauthorized"
     *     }
     */
    /**
     *
     * @param goodsId Id of goods to be deleted
     * @return response of ShoppingCartResponse type
     */

    @DeleteMapping("/shoppingCart/{goodsId}")
    @ResponseBody
    public Response<ShoppingCartResponse> deleteShoppingCart(@PathVariable("goodsId") Long goodsId) {
        return shoppingCartService.deleteGoodsInShoppingCart(goodsId, UserContext.getCurrentUser().getId());
    }
}
